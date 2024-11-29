package com.himym.core.anno

/**
 * @author himym.
 * @description 公共目录类型
 */
sealed class PublicDirectoryType(val value: Int) {
    data object MUSICS : PublicDirectoryType(0)
    data object MOVIES : PublicDirectoryType(1)
    data object PICTURES : PublicDirectoryType(2)
    data object DOWNLOADS : PublicDirectoryType(3)
}