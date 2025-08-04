package top.writerpass.excel.xssf.table

import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

/**
 * 写入既有sheet
 */
fun <T : Any> XSSFSheet.table(
    dataClass: KClass<T>,
    data: List<T>,
    writeMode: WriteMode = WriteMode.CREATE_NEW,
    headerRowIndex: Int? = null
): XSSFSheet {
    val tableAnnotation = dataClass.findAnnotation<ExcelTable>()
    val actualHeaderRowIndex = headerRowIndex ?: tableAnnotation?.headerRowIndex ?: 0
    val tableBuilder = TableBuilder(this, dataClass, actualHeaderRowIndex, writeMode)
    return tableBuilder.build(data)
}

/**
 * 创建新sheet或获取既有sheet
 */
fun <T : Any> XSSFWorkbook.table(
    dataClass: KClass<T>,
    data: List<T>,
    sheetName: String? = null,
    writeMode: WriteMode = WriteMode.CREATE_NEW,
    headerRowIndex: Int? = null
): XSSFSheet {
    val tableAnnotation = dataClass.findAnnotation<ExcelTable>()
    val actualSheetName = sheetName ?: tableAnnotation?.sheetName
    
    val sheet = when (writeMode) {
        WriteMode.CREATE_NEW -> {
            // 尝试删除已存在的sheet
            val existingSheet = getSheet(actualSheetName)
            if (existingSheet != null) {
                removeSheetAt(getSheetIndex(actualSheetName))
            }
            createSheet(actualSheetName)
        }
        WriteMode.APPEND, WriteMode.OVERWRITE -> {
            // 获取或创建sheet
            getSheet(actualSheetName) ?: createSheet(actualSheetName)
        }
    }
    
    return sheet.table(dataClass, data, writeMode, headerRowIndex)
}

/**
 * 扩展函数 - 写入既有sheet
 */
inline fun <reified T : Any> XSSFSheet.table(
    data: List<T>,
    writeMode: WriteMode = WriteMode.CREATE_NEW,
    headerRowIndex: Int? = null
): XSSFSheet {
    return table(T::class, data, writeMode, headerRowIndex)
}

/**
 * 扩展函数 - 创建或获取sheet
 */
inline fun <reified T : Any> XSSFWorkbook.table(
    data: List<T>,
    sheetName: String? = null,
    writeMode: WriteMode = WriteMode.CREATE_NEW,
    headerRowIndex: Int? = null
): XSSFSheet {
    return table(T::class, data, sheetName, writeMode, headerRowIndex)
}