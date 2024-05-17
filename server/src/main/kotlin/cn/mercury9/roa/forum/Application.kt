package cn.mercury9.roa.forum

import Greeting
import SERVER_IP
import SERVER_PORT
import UserData
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.basic
import io.ktor.server.auth.principal
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import sha256

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = SERVER_IP, module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    install(Authentication) {
        basic(name = "userAuth") {
            realm = "user"
            validate { credentials ->
                if (credentials.name.sha256() == credentials.password) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }

    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }

        authenticate("userAuth") {
            get("/login") {
                call.respondText { call.principal<UserIdPrincipal>()?.name ?: "" }
            }

            get("/user") {
                val userName = call.principal<UserIdPrincipal>()?.name
                call.respondText {
                    Json.encodeToString(UserData(userName))
                }
            }
        }

    }
}
