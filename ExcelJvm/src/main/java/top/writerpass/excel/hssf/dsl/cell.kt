package top.writerpass.excel.hssf.dsl

import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DateUtil
import java.util.Date

fun HSSFRow.cell(columnIndex: Int, block: HSSFCell.() -> Unit): HSSFCell {
    val cell = (getCell(columnIndex) ?: createCell(columnIndex))
    cell.block()
    return cell
}

fun HSSFRow.cell(columnIndex: Int): HSSFCell {
    return getCell(columnIndex) ?: createCell(columnIndex)
}

fun HSSFRow.cell(block: HSSFCell.() -> Unit): HSSFCell {
    val id = if (lastCellNum.toInt() == -1) {
        0
    } else {
        lastCellNum.toInt()
    }
    val cell = cell(id)
    cell.block()
    return cell
}

fun HSSFRow.cells(startColumnIndex: Int, endColumnIndex: Int): List<HSSFCell> {
    val cells = mutableListOf<HSSFCell>()
    for (i in startColumnIndex..endColumnIndex) {
        cells += createCell(i)
    }
    return cells.toList()
}

fun HSSFCell.style(block: HSSFCellStyle.() -> Unit): HSSFCellStyle {
    val style = cellStyle.copy()
//    val style = sheet.workbook.createCellStyle()
    val font = sheet.workbook.createFont()
    style.setFont(font)
    style.block()
    cellStyle = style
    return style
}

var HSSFCell.formula: String
    get() = cellFormula
    set(value) {
        cellFormula = value.removePrefix("=")
    }

fun HSSFCell.comment(text: String, author: String = "System") {
    val workbook = sheet.workbook
    val helper = workbook.creationHelper
    val factory = helper.createClientAnchor()

    factory.setCol1(columnIndex)
    factory.setCol2(columnIndex + 2)
    factory.row1 = rowIndex
    factory.row2 = rowIndex + 2

    val comment = sheet.createDrawingPatriarch().createCellComment(factory)
    comment.string = helper.createRichTextString(text)
    comment.author = author
    cellComment = comment
}

//fun HSSFCell.font(block: HSSFFont.() -> Unit) {
//    val workbook = sheet.workbook
//    val font = workbook.createFont()
//    font.block()
//    style {
//        setFont(font)
//    }
//}

//fun HSSFCell.hyperlink(url: String, text: String? = null) {
//    val workbook = sheet.workbook
//    val helper = workbook.creationHelper
//    val hyperlink = helper.createHyperlink(org.apache.poi.common.usermodel.HyperlinkType.URL)
//    hyperlink.address = url
//
//    if (text != null) {
//        setCellValue(text)
//    }
//    this.hyperlink = hyperlink
//
//    style {
//
//    }
//
//    val style = cellStyle ?: workbook.createCellStyle()
//    val font = workbook.createFont()
//    font.underline = org.apache.poi.ss.usermodel.Font.U_SINGLE
//    font.color = org.apache.poi.ss.usermodel.IndexedColors.BLUE.index
//    style.setFont(font)
//    cellStyle = style
//}

var HSSFCell.value: Any?
    get() = when (cellType) {
        CellType.STRING -> stringCellValue
        CellType.NUMERIC -> {
            if (DateUtil.isCellDateFormatted(this)) {
                dateCellValue
            } else {
                numericCellValue
            }
        }

        CellType.BOOLEAN -> booleanCellValue
        CellType.FORMULA -> when (cachedFormulaResultType) {
            CellType.STRING -> stringCellValue
            CellType.NUMERIC -> {
                if (DateUtil.isCellDateFormatted(this)) {
                    dateCellValue
                } else {
                    numericCellValue
                }
            }

            CellType.BOOLEAN -> booleanCellValue
            else -> null
        }

        CellType.BLANK, CellType._NONE, null -> null
        else -> null
    }
    set(value) {
        when (value) {
            null -> setBlank()
            is String -> setCellValue(value)
            is Double -> setCellValue(value)
            is Int -> setCellValue(value.toDouble())
            is Long -> setCellValue(value.toDouble())
            is Float -> setCellValue(value.toDouble())
            is Boolean -> setCellValue(value)
            is Date -> setCellValue(value)
            else -> throw IllegalArgumentException("Unsupported value type: ${value::class}")
        }
    }