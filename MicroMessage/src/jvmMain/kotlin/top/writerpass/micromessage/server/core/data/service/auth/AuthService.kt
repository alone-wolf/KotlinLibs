package top.writerpass.micromessage.server.core.data.service.auth
//package top.writerpass.micromessage.core.data.service.auth
//
//import top.writerpass.micromessage.core.data.base.BaseService
//import top.writerpass.micromessage.core.data.base.dbQuery
//import top.writerpass.micromessage.core.data.service.auth.data.Entity
//import top.writerpass.micromessage.core.data.service.auth.data.LoginSession
//import top.writerpass.micromessage.core.data.service.auth.repository.UserSessionRepository
//
//object AuthService : BaseService {
////    override fun tables(): Array<Table> {
////        return arrayOf(
////            UserAuthCredentialTable,
////            UserSessionTable
////        )
////    }
//
//    suspend fun authCheckWithToken(token: String): LoginSession.Entity? {
//        return dbQuery { UserSessionRepository.selectByToken(token) }
//    }
//
//    data class SQLErrorResult(
//        val service: String,
//        val function: String,
//        val params: String,
//        val error: String
//    )
//
//    data class AuthCheckResult(
//        val passed: Boolean,
//        val level: Int,
//        val userId: Long,
//        val checkedAt: Long,
//        val resultAt: Long
//    ) {
//        companion object {
//            fun passed(
//                userId: Long,
//                checkedAt: Long,
//                resultAt: Long
//            ): AuthCheckResult {
//                return AuthCheckResult(
//                    passed = true,
//                    level = 0,
//                    userId = userId,
//                    checkedAt = checkedAt,
//                    resultAt = resultAt
//                )
//            }
//
//            fun blocked(
//                userId: Long,
//                checkedAt: Long,
//                resultAt: Long
//            ): AuthCheckResult {
//                return AuthCheckResult(
//                    passed = false,
//                    level = 0,
//                    userId = userId,
//                    checkedAt = checkedAt,
//                    resultAt = resultAt
//                )
//            }
//        }
//    }
//
////    suspend fun authCheckWithUsernamePassword(
////        username: String,
////        passwordHash: String
////    ): Result<*> {
////        return dbQuery {
////            UserIdentifierEntity.find {
////                UserIdentifierTable.type eq IdentifierType.Username
////                UserIdentifierTable.content eq username
////                UserIdentifierTable.enabled eq true
////            }.singleOrNull()?.let { identifierEntity ->
////                val identifier = identifierEntity.toData()
////                val userId = identifier.userId
////
////                CredentialEntity.find {
////                    CredentialEntity.Table.userId eq userId
////                    CredentialEntity.Table.type eq CredentialType.Password
////                }.singleOrNull()?.let { credentialEntity ->
////                    val credential = credentialEntity.toData()
////                    val salt = credential.salt
////
////                    val a = (passwordHash + salt).sha256
////                    if (a == credential.passwordHash) {
////                        Result.success(AuthCheckResult.passed())
////                    } else {
////                        Result.success(AuthCheckResult.blocked())
////                    }
////                } ?: Result.failure<SQLDataException>(SQLDataException())
////            } ?: Result.failure<SQLDataException>(SQLDataException())
////        }
////    }
//
//    /**
//     * /auth/register
//     * POST
//     * 用户注册，通常需要邮箱/手机号 + 密码
//     * */
//    suspend fun register() {}
//
//    /**
//     * /auth/login
//     * POST
//     * 用户登录，返回 JWT 或 Session Token
//     * */
//    suspend fun login() {}
//
//    /**
//     * /auth/logout
//     * POST
//     * 注销登录，销毁 token/session
//     * */
//    suspend fun logout() {}
//
//    /**
//     * /auth/refresh
//     * POST
//     * 刷新 token/session
//     * */
//    suspend fun refresh() {}
//
//    /**
//     * /auth/verify
//     * POST
//     * 验证邮箱或手机号验证码
//     * */
//    suspend fun verify() {}
//
//    /**
//     * /auth/forgot-password
//     * POST
//     * 发送重置密码邮件或验证码
//     * */
//    suspend fun forgotPassword() {}
//
//    /**
//     * /auth/reset-password
//     * POST
//     * 提交新密码，完成重置
//     * */
//    suspend fun resetPassword() {}
//}