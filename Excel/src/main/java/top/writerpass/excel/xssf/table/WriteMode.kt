package top.writerpass.excel.xssf.table

enum class WriteMode {
    CREATE_NEW,    // 创建新sheet（清空原有内容）
    APPEND,        // 追加到现有sheet末尾
    OVERWRITE      // 覆盖指定位置（从headerRowIndex开始）
}