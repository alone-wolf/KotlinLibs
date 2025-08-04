package top.writerpass.excel.xssf.table

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExcelColumn(val header: String = "", val order: Int = 0)