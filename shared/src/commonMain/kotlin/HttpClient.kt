import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.headers

expect fun createHttpClient(): HttpClient

object HttpService {
    private var httpClient: HttpClient? = null

    fun getHttpClient(): HttpClient {
        if (httpClient == null) {
            httpClient = createHttpClient()
        }
        return httpClient as HttpClient
    }

    fun setHttpClientWithUserLogin(userName: String, password: String, targetHost: String) {
        httpClient = createHttpClient().config {
            install(Auth) {
                basic {
                    credentials {
                        BasicAuthCredentials(
                            username = userName,
                            password = password
                        )
                    }
                    realm = "user"
                    sendWithoutRequest { request ->
                        request.url.host == targetHost
                    }
                }
            }
        }
    }

    fun updateHttpClientWithLogin(userName: String, password: String, targetHost: String, targetRealm: String) {
        httpClient = getHttpClient().config {
            install(Auth) {
                basic {
                    credentials {
                        BasicAuthCredentials(
                            username = userName,
                            password = password
                        )
                    }
                    realm = targetRealm
                    sendWithoutRequest { request ->
                        request.url.host == targetHost
                    }
                }
            }
        }
    }

    suspend fun get(
        targetUrl: String,
        targetHeaders: Map<String, String> = mapOf()
    ): HttpResponse {
        return getHttpClient().get(targetUrl) {
            headers {
                targetHeaders.forEach { (head, str) ->
                    append(head, str)
                }
            }
        }
    }

}
