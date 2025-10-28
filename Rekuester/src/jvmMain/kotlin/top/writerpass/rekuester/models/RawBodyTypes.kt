package top.writerpass.rekuester.models

sealed interface RawBodyTypes {
    val label: String

    object Text : RawBodyTypes {
        override val label: String = "Text"
    }

    object JavaScript : RawBodyTypes {
        override val label: String = "JavaScript"
    }

    object Json : RawBodyTypes {
        override val label: String = "JSON"
    }

    object Html : RawBodyTypes {
        override val label: String = "HTML"
    }

    object XML : RawBodyTypes {
        override val label: String = "XML"
    }

    companion object Companion {
        val list = listOf(
            Text, JavaScript, Json, Html, XML
        )
        val map = list.associateBy { it.label }
    }
}