package top.writerpass.rekuester.models

sealed interface AuthTypes {
    val label: String

    object InheritAuthFromParent : AuthTypes {
        override val label: String = "Inherit auth from parent"
    }

    object NoAuth : AuthTypes {
        override val label: String = "No authentication"
    }

    object Basic : AuthTypes {
        override val label: String = "Basic Auth"
    }

    object Bearer : AuthTypes {
        override val label: String = "Bearer Token"
    }

    object JWT : AuthTypes {
        override val label: String = "JWT Token"
    }

    object ApiKey : AuthTypes {
        override val label: String = "API Key"
    }

    // 如果你希望支持自定义类型，可以用 data class
    data class Custom(val customLabel: String) : AuthTypes {
        override val label: String = customLabel
    }

    companion object {
        val all = listOf(
            InheritAuthFromParent,
            NoAuth,
            Basic,
            Bearer,
            JWT,
            ApiKey
        )
        val typeMap = all.associateBy { it.label }
    }
}