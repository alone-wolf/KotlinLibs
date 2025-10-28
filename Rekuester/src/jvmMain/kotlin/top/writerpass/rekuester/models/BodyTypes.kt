package top.writerpass.rekuester.models


sealed interface BodyTypes {
    val label: String

    object None : BodyTypes {
        override val label: String = "none"
    }

    object FormData : BodyTypes {
        override val label: String = "form-data"
    }

    object FormUrlencoded : BodyTypes {
        override val label: String = "x-www-form-urlencoded"
    }

    object Raw : BodyTypes {
        override val label: String = "raw"
    }

    object Binary : BodyTypes {
        override val label: String = "binary"
    }

    object GraphQL : BodyTypes {
        override val label: String = "GraphQL"
    }

    companion object Companion {
        val list = listOf(
            None, FormData, FormUrlencoded, Raw, Binary, GraphQL
        )
        val map = list.associateBy { it.label }
    }
}