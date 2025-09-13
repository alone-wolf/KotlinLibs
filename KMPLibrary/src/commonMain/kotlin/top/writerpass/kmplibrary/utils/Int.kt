package top.writerpass.kmplibrary.utils

val Int.fill2Number: String
    get() {
        return toString().padStart(2, '0')
    }