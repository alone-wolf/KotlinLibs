package top.writerpass.excel.xssf.dsl

import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFFont

fun XSSFCell.title1Style() {
    style {
        font {
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            bold = true
            borderTop = BorderStyle.DOUBLE
            borderLeft = BorderStyle.DOUBLE
            borderRight = BorderStyle.DOUBLE
            borderBottom = BorderStyle.DOUBLE
        }
    }
}

fun XSSFCell.title2Style() {
    style {
        font {
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            bold = true
        }
    }
}


fun XSSFCellStyle.font(block: XSSFFont.() -> Unit) {
    font.block()
    setFont(font)
}