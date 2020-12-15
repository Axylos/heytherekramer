package service

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import java.awt.Desktop
import java.net.URI
import java.util.*
import kotlin.concurrent.fixedRateTimer

data class Token(val uri: String, val id: String) {}
data class Poll(val done: Boolean, val accessToken: String) {}

object KramerService {
    private var token: Token? = null
    private var accessToken: String? = null

    val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    suspend fun getToken(): CompletableDeferred<String> = coroutineScope {
        token = async {
            client.get<Token>("https://heytherekramer.com");
        }.await()

        if (token == null) {
            throw RuntimeException("invalid token")
        }
        println(token!!.uri)
        println(token!!.id)

        var done: Boolean = false
        val deferred  = CompletableDeferred<String>()
        fixedRateTimer(
                name = "tokenTimer",
                period = 5000,
                action = {
                    runBlocking {
                        try {

                        val resp = async {
                            val poll = client.get<Poll>("https://heytherekramer.com/poll?id=${token!!.id}&foo=bar")
                                    .also { println(it) }
                            poll
                        }
                            done = resp.await().done
                            accessToken = resp.await().accessToken
                        } catch (err: RuntimeException) {
                            println(err.message)
                        }
                    }
                    println("polling")
                    if (done == true) {
                        println("all done!")
                        cancel()
                        deferred.complete(accessToken ?: "invalid")
                    }
                }
        )
        Desktop.getDesktop().browse(URI(token!!.uri))
        return@coroutineScope deferred
    }
}