package top.writerpass.excel.hssf.dsl

import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet


fun HSSFSheet.row(rowIndex: Int, block: HSSFRow.() -> Unit) {
    (getRow(rowIndex) ?: createRow(rowIndex)).block()
}

fun HSSFSheet.row(block: HSSFRow.() -> Unit) {
    createRow(lastRowNum + 1).block()
}

//fun HSSFSheet.getRow(rowIndex: Int, block: HSSFRow.() -> Unit) {
//    getRow(rowIndex).block()
//}