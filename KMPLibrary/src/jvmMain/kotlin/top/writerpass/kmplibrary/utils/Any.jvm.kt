package top.writerpass.kmplibrary.utils

fun Any.addressString(): String =
    Integer.toHexString(System.identityHashCode(this))