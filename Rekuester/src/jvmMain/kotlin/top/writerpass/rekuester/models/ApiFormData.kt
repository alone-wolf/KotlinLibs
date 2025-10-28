package top.writerpass.rekuester.models

data class ApiFormData(
    val key: String,
    val value: String,
    val description: String,
    val enabled: Boolean
)