import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.headers
import io.ktor.http.parameters

expect fun createHttpClient(): HttpClient

interface HttpService {
    fun getHttpClient(): HttpClient
    fun resetHttpClient()
    fun updateHttpClientWithLogin(
        userName: String,
        password: String,
        targetHost: String,
        targetRealm: String
    )
    suspend fun get(
        targetUrl: String,
        targetHeaders: Map<String, String>? = null
    ): HttpResponse

    suspend fun submitForm(
        targetUrl: String,
        targetHeaders: Map<String, String>? = null,
        targetFormParameters: Map<String, String>
    ): HttpResponse
}

class DefaultHttpService: HttpService {
    private var httpClient: HttpClient? = null

    override fun getHttpClient(): HttpClient {
        if (httpClient == null) {
            httpClient = createHttpClient()
        }
        return httpClient as HttpClient
    }

    override fun resetHttpClient() {
        httpClient = createHttpClient()
    }

    override fun updateHttpClientWithLogin(userName: String, password: String, targetHost: String, targetRealm: String) {
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

    override suspend fun get(
        targetUrl: String,
        targetHeaders: Map<String, String>?
    ): HttpResponse {
        return getHttpClient().get(targetUrl) {
            headers {
                targetHeaders?.let {
                    targetHeaders.forEach { (head, str) ->
                        append(head, str)
                    }
                }
            }
        }
    }

    override suspend fun submitForm(
        targetUrl: String,
        targetHeaders: Map<String, String>?,
        targetFormParameters: Map<String, String>
    ): HttpResponse {
        return getHttpClient().submitForm(
            url = targetUrl,
            formParameters = parameters {
                targetFormParameters.forEach { (key, value) ->
                    append(key, value)
                }
            }
        ) {
            headers {
                targetHeaders?.let {
                    targetHeaders.forEach { (head, str) ->
                        append(head, str)
                    }
                }
            }
        }
    }

}
