package com.himym.core.extension

/**
 * @author himym.
 * @description replace if(){}..else{}
 */
sealed class BooleanExtension<out T>

class DataTransformer<T>(val data: T) : BooleanExtension<T>()

data object Otherwise : BooleanExtension<Nothing>()

inline fun <T> Boolean.yes(block: () -> T): BooleanExtension<T> =
    when {
        this -> DataTransformer(block())
        else -> Otherwise
    }

inline fun <T> BooleanExtension<T>.otherwise(block: () -> T): T =
    when (this) {
        is Otherwise -> block()
        is DataTransformer<T> -> data
    }