package top.writerpass.rsynckmp

fun rsync(block: RsyncBuilder.() -> Unit): RsyncCommand =
    RsyncBuilder().apply(block).build()