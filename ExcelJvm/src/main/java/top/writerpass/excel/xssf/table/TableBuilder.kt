package top.writerpass.excel.xssf.table

import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import kotlin.collections.forEach
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

class TableBuilder<T : Any>(
    private val sheet: XSSFSheet,
    private val dataClass: KClass<T>,
    headerRowIndex: Int = 0,
    private val writeMode: WriteMode = WriteMode.CREATE_NEW
) {

    private val headers = mutableListOf<Pair<String, Int>>()
    private var currentRow = 0

    init {
        analyzeDataClass()
        currentRow = when (writeMode) {
            WriteMode.APPEND -> sheet.lastRowNum + 1
            WriteMode.OVERWRITE -> headerRowIndex
            WriteMode.CREATE_NEW -> headerRowIndex
        }
    }

    private fun analyzeDataClass() {
        val properties = dataClass.memberProperties

        properties.forEach { prop ->
            val annotation = prop.findAnnotation<ExcelColumn>()
            val headerName = annotation?.header?.takeIf { it.isNotEmpty() } ?: prop.name
            val order = annotation?.order ?: 0

            headers.add(headerName to order)
        }

        headers.sortBy { it.second }
    }

    fun build(data: List<T>): XSSFSheet {
        createHeaderRow()
        fillData(data)
        autoSizeColumns()
        return sheet
    }

    private fun createHeaderRow() {
        when (writeMode) {
            WriteMode.CREATE_NEW -> {
                // 清空sheet内容
                for (i in sheet.physicalNumberOfRows - 1 downTo 0) {
                    sheet.removeRow(sheet.getRow(i))
                }
                val row = sheet.createRow(currentRow++)
                createHeaderCells(row)
            }
            WriteMode.OVERWRITE -> {
                // 覆盖指定位置
                val row = sheet.getRow(currentRow) ?: sheet.createRow(currentRow)
                row?.let { createHeaderCells(it) }
                currentRow++
            }
            WriteMode.APPEND -> {
                // 只在APPEND模式下创建表头（如果当前sheet为空）
                if (sheet.physicalNumberOfRows == 0 || currentRow == 0) {
                    val row = sheet.createRow(currentRow++)
                    createHeaderCells(row)
                } else {
                    // 如果已有数据，不创建表头直接跳过
                }
            }
        }
    }

    private fun createHeaderCells(row: XSSFRow) {
        headers.forEachIndexed { index, (headerName, _) ->
            val cell = row.getCell(index) ?: row.createCell(index)
            cell.setCellValue(headerName)

            val style = sheet.workbook.createCellStyle()
            val font = sheet.workbook.createFont()
            font.bold = true
            style.setFont(font)
            cell.cellStyle = style
        }
    }

    private fun fillData(data: List<T>) {
        data.forEach { item ->
            val row = sheet.createRow(currentRow++)

            val properties = dataClass.memberProperties
                .sortedBy { it.findAnnotation<ExcelColumn>()?.order ?: 0 }

            properties.forEachIndexed { index, prop ->
                val cell = row.createCell(index)
                val value = prop.getter.call(item)

                when (value) {
                    is String -> cell.setCellValue(value)
                    is Number -> cell.setCellValue(value.toDouble())
                    is Boolean -> cell.setCellValue(value)
                    is Date -> cell.setCellValue(value)
                    is LocalDate -> cell.setCellValue(value.toString())
                    is LocalDateTime -> cell.setCellValue(value.toString())
                    null -> cell.setCellValue("")
                    else -> cell.setCellValue(value.toString())
                }
            }
        }
    }

    private fun autoSizeColumns() {
        if (headers.isNotEmpty()) {
            repeat(headers.size) { colIndex ->
                sheet.autoSizeColumn(colIndex)
            }
        }
    }
}