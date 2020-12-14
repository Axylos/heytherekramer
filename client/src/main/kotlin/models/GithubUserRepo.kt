package models

import com.draketalley.kramer.KramerDb
import com.draketalley.kramer.sqldelight.GithubUser
import com.draketalley.kramer.sqldelight.GithubUserQueries

object GithubUserRepo {
    val queries: GithubUserQueries = KramerDb(DbConn.driver)
        .githubUserQueries

    fun fetchAll() {
       queries.select()
    }

    fun storeToken(token: String): Long {
        var id = 0L
        queries.transaction {
            queries.saveToken(token)
            id = queries.findLast().executeAsOne()
        }

        return id
    }

    fun create(data: GithubUser) {
        queries.insert(data)
    }

    fun hasToken(): Boolean {
        return queries.hasToken().executeAsOne() > 0
    }

    fun reset() {
        queries.reset()
    }

    fun getToken(): String {
        return queries.getToken().executeAsOne()
    }

    fun saveLogin(login: String) {
        queries.storeLogin(login)
    }
}
