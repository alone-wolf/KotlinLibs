package top.writerpass.rsynckmp


@DslMarker
annotation class RsyncDsl

@RsyncDsl
class RsyncBuilder {
    private val sources = mutableListOf<String>()
    private var dest: String? = null
    private val options = mutableListOf<String>()
    private var sshConfig: SshConfig? = null

    var archive = false
    var compress = false
    var progress = false
    var verbose = false
    var recursive = false
    var checksum = false
    var dryRun = false
    var delete = false

    fun source(path: String) {
        sources += path
    }

    fun destination(path: String) {
        dest = path
    }

    fun exclude(pattern: String) {
        options += "--exclude=$pattern"
    }

    fun include(pattern: String) {
        options += "--include=$pattern"
    }

    fun bwlimit(kbps: Int) {
        options += "--bwlimit=$kbps"
    }

    fun partial(enabled: Boolean = true) {
        if (enabled) options += "--partial"
    }

    fun ssh(block: SshConfigBuilder.() -> Unit) {
        sshConfig = SshConfigBuilder().apply(block).build()
    }

    fun option(value: String) {
        options += value
    }

    fun build(): RsyncCommand {
        require(sources.isNotEmpty()) { "At least one source() must be provided" }
        val dstVal = requireNotNull(dest) { "destination() must be specified" }

        if (archive) options += "-a"
        if (compress) options += "-z"
        if (progress) options += "--progress"
        if (verbose) options += "-v"
        if (recursive) options += "-r"
        if (checksum) options += "--checksum"
        if (dryRun) options += "--dry-run"
        if (delete) options += "--delete"

        return RsyncCommand(sources, dstVal, options, sshConfig)
    }
}
