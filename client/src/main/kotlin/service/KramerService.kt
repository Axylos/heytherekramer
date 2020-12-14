package service

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.net.URI
import kotlin.concurrent.fixedRateTimer

data class Token(val uri: String, val id: String) {}
data class Poll(val done: Boolean) {}

object KramerService {
    private var token: Token? = null

    val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    suspend fun getToken() = coroutineScope {
        token = async {
            client.get<Token>("https://heytherekramer.com");
        }.await()

        if (token == null) {
            throw RuntimeException("invalid token")
        }
        println(token!!.uri)
        println(token!!.id)

        fixedRateTimer(
                name = "tokenTimer",
                period = 5000,
                action = {
                    launch {
                        val resp = async {
                            client.get<Poll>("https://heytherekramer.com/poll")
                                    .also { println(it) }
                        }
                    }
                }
        )
        Desktop.getDesktop().browse(URI(token!!.uri))
    }
}