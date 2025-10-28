package top.writerpass.rekuester.models

sealed interface RawBodyType {
    val label: String

    object Text : RawBodyType {
        override val label: String = "Text"
    }

    object JavaScript : RawBodyType {
        override val label: String = "JavaScript"
    }

    object Json : RawBodyType {
        override val label: String = "JSON"
    }

    object Html : RawBodyType {
        override val label: String = "HTML"
    }

    object XML : RawBodyType {
        override val label: String = "XML"
    }

    companion object {
        val list = listOf(
            Text, JavaScript, Json, Html, XML
        )
        val map = list.associateBy { it.label }
    }
}