package top.writerpass.rsynckmp

data class RsyncCommand(
    val sources: List<String>,
    val destination: String,
    val options: List<String>,
    val ssh: SshConfig? = null
) {
    fun toCommandString(): String {
        val base = mutableListOf("rsync")
        base += options
        ssh?.let { base += listOf("-e", it.toCommandPart()) }
        base += sources
        base += destination
        return base.joinToString(" ")
    }
}