package service

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import models.GithubUserRepo

data class GithubUserResponse(val login: String) {}

object GithubService {
    public var token: String = "invalid"

    init {
        token = GithubUserRepo.getToken()
    }
    val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    suspend fun getUser() = coroutineScope {
        val resp = async {
            client.get<GithubUserResponse>("https://api.github.com/user") {
                header("Authorization", "token $token")
                header("Accept", "application/vnd.github.v3+json")
            }
                .also { GithubUserRepo.saveLogin(it.login) }
        }

        val name = resp.await()
            .login
        println(name)
        println("ok")
    }
}