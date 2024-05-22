package cn.mercury9.roa.forum

import Greeting
import SERVER_IP
import SERVER_PORT
import cn.mercury9.roa.forum.database.DatabaseSingleton
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.basic
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import sha256

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = SERVER_IP, module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    DatabaseSingleton.init()

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

    install(Routing) {}

    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
    }

    routeLogin()
    routeUser()
}
