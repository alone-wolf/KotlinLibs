package top.writerpass.rekuester.models

sealed interface BodyType {
    val label: String

    object None : BodyType {
        override val label: String = "none"
    }

    object FormData : BodyType {
        override val label: String = "form-data"
    }

    object FormUrlencoded : BodyType {
        override val label: String = "x-www-form-urlencoded"
    }

    object Raw : BodyType {
        override val label: String = "raw"
    }

    object Binary : BodyType {
        override val label: String = "binary"
    }

    object GraphQL : BodyType {
        override val label: String = "GraphQL"
    }

    companion object {
        val list = listOf(
            None, FormData, FormUrlencoded, Raw, Binary, GraphQL
        )
        val map = list.associateBy { it.label }
    }
}