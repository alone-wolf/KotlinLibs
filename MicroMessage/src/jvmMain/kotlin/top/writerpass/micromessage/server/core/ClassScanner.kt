package top.writerpass.micromessage.server.core

import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import org.jetbrains.exposed.sql.Table
import org.slf4j.LoggerFactory
import top.writerpass.micromessage.server.core.data.base.BaseRouting
import top.writerpass.micromessage.server.utils.WithLogger
import top.writerpass.micromessage.server.utils.logWrapper

class ClassScanner : WithLogger {
    override val logger = LoggerFactory.getLogger(ClassScanner::class.simpleName)

    private val SERVICE_PACKAGE = "top.writerpass.micromessage.server.core.data.service"
    val tables: Array<Table>
    val routings: Array<BaseRouting>

    init {
        logWrapper("scan classes under [$SERVICE_PACKAGE]") {
            val scanResult = ClassGraph()
                .enableClassInfo()
                .acceptPackages(SERVICE_PACKAGE)
                .scan()
            val tableInstances = scanResult.tables().also { its ->
                its.forEach { it ->
                    "Table: [${it::class.qualifiedName}]".logi()
                }
            }

            val routingInstances = scanResult.routings().also { its ->
                its.forEach { it ->
                    "Routing: [${it::class.qualifiedName}]".logi()
                }
            }
            tableInstances to routingInstances
        }.also {
            tables = it.first
            routings = it.second
        }
    }

    private fun ScanResult.tables(): Array<Table> {
        return getSubclasses(Table::class.qualifiedName)
            .loadClasses(Table::class.java)
            .filterNot { it.isInterface }
            .mapNotNull { clazz -> clazz.kotlin.objectInstance }
            .toTypedArray()
    }

    private fun ScanResult.routings(): Array<BaseRouting> {
        return getClassesImplementing(BaseRouting::class.qualifiedName)
            .loadClasses(BaseRouting::class.java)
            .filterNot { it.isInterface }
            .mapNotNull { clazz -> clazz.kotlin.objectInstance }
            .toTypedArray()
    }
}