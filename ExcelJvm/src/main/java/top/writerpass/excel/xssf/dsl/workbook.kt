package top.writerpass.excel.xssf.dsl

import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File

fun XSSFWorkbook.autoClose(block: (XSSFWorkbook) -> Unit) {
    block(this)
    close()
}
fun xlsx(block: XSSFWorkbook.() -> Unit) {
    val workbook = XSSFWorkbook()
    workbook.block()
    workbook.close()
}

fun xlsx(path: String, block: XSSFWorkbook.() -> Unit) {
    val workbook = XSSFWorkbook(path)
    workbook.block()
    workbook.close()
}

fun xlsx(workbook: XSSFWorkbook, block: XSSFWorkbook.() -> Unit) {
    workbook.block()
    workbook.close()
}

fun xlsx(file: File, block: XSSFWorkbook.() -> Unit) {
    val workbook = XSSFWorkbook(file)
    workbook.block()
    workbook.close()
}

fun xlsxNoClose(block: XSSFWorkbook.() -> Unit): XSSFWorkbook {
    val workbook = XSSFWorkbook()
    workbook.block()
    return workbook
}

fun xlsxNoClose(path: String, block: XSSFWorkbook.() -> Unit): XSSFWorkbook {
    val workbook = XSSFWorkbook(path)
    workbook.block()
    return workbook
}

fun xlsxNoClose(file: File, block: XSSFWorkbook.() -> Unit): XSSFWorkbook {
    val workbook = XSSFWorkbook(file)
    workbook.block()
    return workbook
}

fun xlsxNoClose(workbook: XSSFWorkbook, block: XSSFWorkbook.() -> Unit): XSSFWorkbook {
    workbook.block()
    return workbook
}

fun XSSFWorkbook.save(path: String) = save(File(path))

fun XSSFWorkbook.save(file: File) {
    write(file.normalize().outputStream())
}

fun XSSFWorkbook.sheet(name: String, block: XSSFSheet.() -> Unit): XSSFSheet {
    val sheet = (getSheet(name) ?: createSheet(name))
    sheet.block()
    return sheet
}

//fun XSSFWorkbook.getSheet(name: String, block: XSSFSheet.() -> Unit) {
//    getSheet(name).block()
//}