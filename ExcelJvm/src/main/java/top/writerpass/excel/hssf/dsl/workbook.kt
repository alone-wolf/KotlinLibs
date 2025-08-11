package top.writerpass.excel.hssf.dsl

import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File

fun HSSFWorkbook.autoClose(block: (HSSFWorkbook) -> Unit) {
    block(this)
    close()
}

fun xls(block: HSSFWorkbook.() -> Unit) {
    val workbook = HSSFWorkbook()
    workbook.block()
    workbook.close()
}

//fun xls(path: String, block: HSSFWorkbook.() -> Unit) {
//    val workbook = HSSFWorkbook(path)
//    workbook.block()
//    workbook.close()
//}

fun xls(workbook: HSSFWorkbook, block: HSSFWorkbook.() -> Unit) {
    workbook.block()
    workbook.close()
}

//fun xls(file: File, block: HSSFWorkbook.() -> Unit) {
//    val workbook = HSSFWorkbook(file)
//    workbook.block()
//    workbook.close()
//}

fun xlsNoClose(block: HSSFWorkbook.() -> Unit): HSSFWorkbook {
    val workbook = HSSFWorkbook()
    workbook.block()
    return workbook
}

//fun xlsNoClose(path: String, block: HSSFWorkbook.() -> Unit): HSSFWorkbook {
//    val workbook = HSSFWorkbook(path)
//    workbook.block()
//    return workbook
//}

//fun xlsNoClose(file: File, block: HSSFWorkbook.() -> Unit): HSSFWorkbook {
//    val workbook = HSSFWorkbook(file)
//    workbook.block()
//    return workbook
//}

fun xlsNoClose(workbook: HSSFWorkbook, block: HSSFWorkbook.() -> Unit): HSSFWorkbook {
    workbook.block()
    return workbook
}

fun HSSFWorkbook.save(path: String) = save(File(path))

fun HSSFWorkbook.save(file: File) {
    write(file.normalize().outputStream())
}

fun HSSFWorkbook.sheet(name: String, block: HSSFSheet.() -> Unit): HSSFSheet {
    val sheet = (getSheet(name) ?: createSheet(name))
    sheet.block()
    return sheet
}

//fun HSSFWorkbook.getSheet(name: String, block: HSSFSheet.() -> Unit) {
//    getSheet(name).block()
//}