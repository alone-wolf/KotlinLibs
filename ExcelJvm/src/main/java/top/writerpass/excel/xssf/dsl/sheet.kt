package top.writerpass.excel.xssf.dsl

import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFSheet


fun XSSFSheet.mergeCells(startRow: Int, startColumn: Int, endRow: Int, endColumn: Int) {
    addMergedRegion(CellRangeAddress(startRow, endRow, startColumn, endColumn))
}

fun XSSFSheet.mergeCells(range: CellRangeAddress) {
    addMergedRegion(range)
}

fun XSSFSheet.copyFrom(sourceSheet: XSSFSheet) {
    val targetWorkbook = this.workbook

    // 创建 CellStyle 映射（source -> target），避免重复转换
    val styleMap = mutableMapOf<CellStyle, CellStyle>()

    // 拷贝行与单元格
    for (rowIndex in 0..sourceSheet.lastRowNum) {
        val srcRow = sourceSheet.getRow(rowIndex) ?: continue
        val destRow = this.createRow(rowIndex).apply {
            height = srcRow.height
        }

        for (cellIndex in srcRow.firstCellNum.toInt() until srcRow.lastCellNum.toInt()) {
            val srcCell = srcRow.getCell(cellIndex) ?: continue
            val destCell = destRow.createCell(cellIndex)

            // 拷贝样式
            val srcStyle = srcCell.cellStyle
            val destStyle = styleMap.getOrPut(srcStyle) {
                val newStyle = targetWorkbook.createCellStyle()
                newStyle.cloneStyleFrom(srcStyle)
                newStyle
            } as XSSFCellStyle
            destCell.cellStyle = destStyle

            // 拷贝内容
            when (srcCell.cellType) {
                CellType.STRING -> destCell.setCellValue(srcCell.stringCellValue)
                CellType.NUMERIC -> {
                    if (DateUtil.isCellDateFormatted(srcCell)) {
                        destCell.setCellValue(srcCell.dateCellValue)
                    } else {
                        destCell.setCellValue(srcCell.numericCellValue)
                    }
                }

                CellType.BOOLEAN -> destCell.setCellValue(srcCell.booleanCellValue)
                CellType.FORMULA -> destCell.cellFormula = srcCell.cellFormula
                CellType.ERROR -> destCell.setCellErrorValue(srcCell.errorCellValue)
                CellType.BLANK -> destCell.setBlank()
                else -> {}
            }

            // 拷贝批注（可选）
            if (srcCell.cellComment != null) {
                destCell.cellComment = srcCell.cellComment
            }
        }
    }

    // 拷贝合并区域
    for (i in 0 until sourceSheet.numMergedRegions) {
        val region = sourceSheet.getMergedRegion(i)
        this.addMergedRegion(region.copy())
    }

    // 拷贝列宽
    val maxColumnNum = (0..sourceSheet.lastRowNum)
        .mapNotNull { sourceSheet.getRow(it)?.lastCellNum?.toInt() }
        .maxOrNull() ?: 0

    for (col in 0..maxColumnNum) {
        this.setColumnWidth(col, sourceSheet.getColumnWidth(col))
        this.setColumnHidden(col, sourceSheet.isColumnHidden(col))
    }

    // 拷贝 Sheet 级别的设置（如页边距等）
    this.defaultColumnWidth = sourceSheet.defaultColumnWidth
    this.defaultRowHeight = sourceSheet.defaultRowHeight
    this.isRightToLeft = sourceSheet.isRightToLeft
}