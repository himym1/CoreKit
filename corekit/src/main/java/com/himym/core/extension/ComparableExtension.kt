package com.himym.core.extension

fun <T : Comparable<T>> T.ifLess(other: T, default: () -> T): T {
    return if (compareTo(other) < 0) default() else this
}

fun <T : Comparable<T>> T.ifEqual(other: T, default: () -> T): T {
    return if (compareTo(other) == 0) default() else this
}

fun <T : Comparable<T>> T.ifLarger(other: T, default: () -> T): T {
    return if (compareTo(other) > 0) default() else this
}