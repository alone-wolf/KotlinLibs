package top.writerpass.kotlinlibrary.shell.platform_tools

open class Commands private constructor(
    open val args: MutableList<String> = mutableListOf()
) {

    companion object{
        fun builder(){
            
        }
    }

    fun build(): List<String> {
        return args
    }

    override fun toString(): String = build().joinToString(" ")
}
