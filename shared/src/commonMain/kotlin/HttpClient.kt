import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

expect fun getHttpClient(): HttpClient

object HttpService {
    private var httpClient: HttpClient? = null

    suspend fun setHttpClientWithUserLogin(userName: String, password: String, targetUrl: String): String {
        httpClient = getHttpClient().config {
            install(Auth) {
                basic {
                    credentials {
                        BasicAuthCredentials(
                            username = userName,
                            password = password
                        )
                    }
                    realm = "user"
                }
            }
        }
        val response = httpClient!!.get(targetUrl)
        return response.bodyAsText()
    }

    suspend fun get(targetUrl: String): HttpResponse {
        if (httpClient == null) httpClient = getHttpClient()
        return httpClient!!.get(targetUrl)
    }

}
