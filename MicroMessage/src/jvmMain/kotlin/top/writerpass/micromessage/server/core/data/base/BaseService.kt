package top.writerpass.micromessage.server.core.data.base

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import top.writerpass.micromessage.server.core.Singletons

interface BaseService

suspend inline fun <reified T> BaseService.dbQuery(
    noinline block: suspend Transaction.() -> T
): T = newSuspendedTransaction(
    context = Dispatchers.IO,
    db = Singletons.databaseContainer.database,
    statement = block
)