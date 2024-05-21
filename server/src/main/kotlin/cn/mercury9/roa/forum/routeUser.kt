package cn.mercury9.roa.forum

import data.UserData
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Application.routeUser() {
    routing {
        route("/user") {
            authenticate {
                get {
                    val userName = call.principal<UserIdPrincipal>()?.name
                    call.respondText {
                        Json.encodeToString(
                            UserData(
                                userName = userName
                            )
                        )
                    }
                }
            }

            get("/{userName}") {
                val userName = call.parameters["userName"]
                call.response.status(HttpStatusCode.NotImplemented)
                call.respondText {
                    ""
                }
            }
        }
    }
}
