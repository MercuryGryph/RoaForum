import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

actual fun getHttpClient(): HttpClient {
    return HttpClient(CIO)
}