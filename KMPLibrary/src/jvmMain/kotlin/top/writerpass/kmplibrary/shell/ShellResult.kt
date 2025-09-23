package top.writerpass.kmplibrary.shell

data class ShellResult(
    val exitCode: Int,
    val stdout: String,
    val stderr: String
)
