package top.writerpass.excel.xssf.table

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExcelTable(
    val sheetName: String = "Sheet1",
    val headerRowIndex: Int = 0
)