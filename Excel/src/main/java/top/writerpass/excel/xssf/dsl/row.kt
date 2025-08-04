package top.writerpass.excel.xssf.dsl

import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet

fun XSSFSheet.row(rowIndex: Int, block: XSSFRow.() -> Unit) {
    (getRow(rowIndex) ?: createRow(rowIndex)).block()
}

fun XSSFSheet.row(block: XSSFRow.() -> Unit) {
    createRow(lastRowNum + 1).block()
}

//fun XSSFSheet.getRow(rowIndex: Int, block: XSSFRow.() -> Unit) {
//    getRow(rowIndex).block()
//}