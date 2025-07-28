package top.writerpass.ktorserverjvm.auth

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserPasswordCredential
import io.ktor.server.auth.basic

fun Application.setupBasicAuthentication(
    basicAuthName: String? = null,
    realm: String = "Ktor Server",
    block: suspend ApplicationCall.(UserPasswordCredential) -> Any?
) {
    install(Authentication) {
        basic(basicAuthName) {
            this.realm = realm
            validate(block)
        }
    }
}