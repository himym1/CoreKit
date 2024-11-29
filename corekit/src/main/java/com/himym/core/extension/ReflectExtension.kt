package com.himym.core.extension

import java.lang.ref.WeakReference
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.javaType

/**
 * @author himym.
 * @description 反射扩展函数
 */

/**
 * 用于标记字段的注解，可以指定字段名称和访问权限。
 *
 * @param name 字段名称，默认为空字符串。如果不设置，会使用字段本身的名称。
 * @param isAccessible 是否设置字段为可访问（包括私有字段）。默认为 `false`。
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class FieldName(val name: String = "", val isAccessible: Boolean = false)

/**
 * 泛型引用接口，用于封装对象字段的 `get` 和 `set` 操作。
 */
interface Ref<T> {
    /** 获取字段的值。 */
    fun get(): T?

    /** 设置字段的值。 */
    fun set(value: T?)

    /**
     * 如果字段值不为 null，则执行指定操作。
     *
     * @param onGet 回调操作，接收字段的非 null 值。
     */
    fun getNotNull(onGet: (T) -> Unit) {
        get()?.let(onGet)
    }
}

/**
 * 字段引用的实现类，基于反射操作封装字段的 `get` 和 `set` 方法。
 *
 * @param field 目标字段。
 * @param target 目标对象的弱引用，避免内存泄漏。
 */
class FieldRef<T>(val field: Field, val target: WeakReference<Any>) : Ref<T> {

    /** 构造函数，支持直接传递目标对象。 */
    constructor(field: Field, target: Any) : this(field, WeakReference(target))

    override fun get(): T? = tryOrNull { field.get(target.get()) as T? }

    override fun set(value: T?) {
        tryOr { field.set(target.get(), value) }
    }
}

/**
 * 获取当前对象的所有字段，包括继承字段（可选）。
 *
 * @param withSuper 是否包含继承字段，默认为 `true`。
 * @param filter 字段过滤器，默认为接收所有字段。
 * @return 符合条件的字段列表。
 */
fun Any.fields(withSuper: Boolean = true, filter: (Field) -> Boolean = { true }): List<Field> =
    HashSet<Field>().also { set ->
        fun checkAndAdd(field: Field) {
            if (!set.contains(field) && filter(field)) set.add(field)
        }

        fun Class<*>.checkAndAdd() {
            fields.forEach(::checkAndAdd)
            declaredFields.forEach(::checkAndAdd)
        }

        var cls: Class<*>? = javaClass
        do {
            cls?.checkAndAdd()
            cls = cls?.superclass
        } while (withSuper && null != cls)

    }.toList()

/**
 * 获取对象的指定字段。
 *
 * @param name 字段名称。
 * @param isAccessible 是否设置为可访问，默认为 `true`。
 * @return 目标字段或 `null`（如果未找到）。
 */
fun Any.field(name: String, isAccessible: Boolean = true): Field? =
    this.javaClass.field(name, isAccessible)

/**
 * 获取类的指定字段。
 *
 * @param name 字段名称。
 * @param isAccessible 是否设置为可访问，默认为 `true`。
 * @return 目标字段或 `null`（如果未找到）。
 */
fun Class<*>.field(name: String, isAccessible: Boolean = true): Field? =
    field(name)?.apply {
        if (!this.isAccessible && isAccessible) this.isAccessible = true
    }

/**
 * 获取类的指定字段（不强制可访问）。
 *
 * @param name 字段名称。
 * @return 目标字段或 `null`（如果未找到）。
 */
fun Class<*>.field(name: String): Field? =
    tryOr { getField(name) } ?: tryOr { getDeclaredField(name) } ?: superclass?.field(name)

/**
 * 获取对象的指定方法。
 *
 * @param name 方法名称。
 * @param isAccessible 是否设置为可访问，默认为 `true`。
 * @param argTypes 方法参数类型。
 * @return 目标方法或 `null`（如果未找到）。
 */
fun Any.method(
    name: String,
    isAccessible: Boolean = true,
    vararg argTypes: Class<*>
): Method? =
    this.javaClass.method(name, isAccessible, *argTypes)

/**
 * 获取类的指定方法。
 *
 * @param name 方法名称。
 * @param isAccessible 是否设置为可访问，默认为 `true`。
 * @param argTypes 方法参数类型。
 * @return 目标方法或 `null`（如果未找到）。
 */
fun Class<*>.method(
    name: String,
    isAccessible: Boolean = true,
    vararg argTypes: Class<*>
): Method? =
    method(name, *argTypes)?.apply {
        if (!this.isAccessible && isAccessible) this.isAccessible = true
    }

/**
 * 获取类的指定方法（不强制可访问）。
 *
 * @param name 方法名称。
 * @param argTypes 方法参数类型。
 * @return 目标方法或 `null`（如果未找到）。
 */
fun Class<*>.method(name: String, vararg argTypes: Class<*>): Method? =
    tryOr { getMethod(name, *argTypes) } ?: tryOr { getDeclaredMethod(name, *argTypes) }
    ?: superclass?.method(name, *argTypes)

/**
 * 使用反射调用对象的方法。
 *
 * @param name 方法名称。
 * @param isAccessible 是否设置为可访问，默认为 `true`。
 * @param args 方法参数。
 * @return 方法的返回值或 `null`。
 */
inline fun <reified R> Any.func(
    name: String,
    isAccessible: Boolean = true,
    vararg args: Any
): R? =
    method(name, isAccessible, *args.map { it.javaClass }.toTypedArray())?.invoke(
        this,
        *args
    ) as R?

/**
 * 基于反射动态实例化一个类。
 *
 * @param cls 要实例化的类。
 * @param args 参数键值对，按构造函数参数名匹配。
 * @return 实例化对象或 `null`。
 */
fun <T : Any> newInstanceByReflect(cls: KClass<T>, args: Map<String, String?>): T? =
    cls.constructors.firstOrNull()?.let { constructor ->
        constructor.callBy(constructor.parameters.associateWith { param ->
            param.type.convert(args[param.name])
        })    }

/**
 * 动态注入字段，支持 `@FieldName` 注解配置。
 */
fun Any.injectReflect() {
    this.fields { field -> null != field.getAnnotation(FieldName::class.java) }
        .forEach { field ->
            val anno = field.getAnnotation(FieldName::class.java)
            javaClass.superclass.field(anno.name.ifEmpty { field.name }, anno.isAccessible)
                ?.also { superField ->
                    superField.set(this, FieldRef<Any>(superField, this))
                }
        }
}

@OptIn(ExperimentalStdlibApi::class)
fun KType.convert(value: String?): Any? {
    return when (this.javaType) {
        String::class.java -> value
        Int::class.java -> value?.toIntOrNull()
        Long::class.java -> value?.toLongOrNull()
        Boolean::class.java -> value?.toBoolean()
        Double::class.java -> value?.toDoubleOrNull()
        Float::class.java -> value?.toFloatOrNull()
        else -> null // 添加更多类型支持时扩展此处
    }
}