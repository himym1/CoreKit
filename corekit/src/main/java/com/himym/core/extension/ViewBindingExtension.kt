package com.topping.core.extension

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified VB : ViewBinding> Fragment.viewBinding() = FragmentViewBindingDelegate(this, VB::class.java)

class FragmentViewBindingDelegate<VB : ViewBinding>(
    private val fragment: Fragment,
    private val bindingClass: Class<VB>
) : ReadOnlyProperty<Fragment, VB> {

    private var binding: VB? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        binding?.let { return it }

        val method = bindingClass.getMethod("inflate", LayoutInflater::class.java)
        val invokeBinding = method.invoke(null, thisRef.layoutInflater) as VB
        binding = invokeBinding
        return invokeBinding
    }
}