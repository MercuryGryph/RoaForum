package cn.mercury9.roa.forum

import io.ktor.server.application.Application
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.response.respondText
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.routing.get

fun Application.routeLogin() {
    routing {
        route("/login") {
            authenticate("userAuth") {
                get {
                    call.respondText {
                        call.principal<UserIdPrincipal>()?.name ?: ""
                    }
                }
            }
        }
    }
}