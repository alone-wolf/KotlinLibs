package top.writerpass.kmplibrary.utils

//
//class ShellTaskResult(
//    val std: List<String>,
//    val err: List<String>,
//    val code: Int
//) {
//    fun toJsonString(): String {
//        return Singletons.json.encodeToString(this)
//    }
//
//    fun switch(onOk: (ShellTaskResult) -> Unit, onErr: (ShellTaskResult) -> Unit) {
//        if (code == 0) onOk(this) else onErr(this)
//    }
//}