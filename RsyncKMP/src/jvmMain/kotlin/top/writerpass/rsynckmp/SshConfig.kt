package top.writerpass.rsynckmp

data class SshConfig(
    val user: String?,
    val host: String?,
    val port: Int?,
    val privateKey: String?
) {
    fun toCommandPart(): String {
        val base = mutableListOf("ssh")
        if (port != null) base += "-p $port"
        if (privateKey != null) base += "-i ${privateKey.quoteIfNeeded()}"
        return base.joinToString(" ")
    }
}

@RsyncDsl
class SshConfigBuilder {
    var user: String? = null
    var host: String? = null
    var port: Int? = null
    var privateKey: String? = null
    fun build() = SshConfig(user, host, port, privateKey)
}

private fun String.quoteIfNeeded() = if (contains(" ")) "\"$this\"" else this
