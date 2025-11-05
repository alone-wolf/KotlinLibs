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

    object CSS : RawBodyTypes {
        override val label: String = "CSS"
    }

    companion object Companion {
        val list = listOf(
            Text, JavaScript, Json, Html, XML,CSS
        )
        val map = list.associateBy { it.label }
    }
}