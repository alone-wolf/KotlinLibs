package top.writerpass.excel.sample

import top.writerpass.excel.xssf.dsl.cell
import top.writerpass.excel.xssf.dsl.row
import top.writerpass.excel.xssf.dsl.save
import top.writerpass.excel.xssf.dsl.sheet
import top.writerpass.excel.xssf.dsl.value
import top.writerpass.excel.xssf.dsl.xlsx

fun main() {
    xlsx {
        sheet("Sheet1") {
            row {
                cell {
                    value = "Hello"
                }
            }
        }
        save("test.xlsx")
    }
}