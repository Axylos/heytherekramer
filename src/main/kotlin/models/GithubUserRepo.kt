package models

import com.draketalley.kramer.KramerDb
import com.draketalley.kramer.sqldelight.GithubUser
import com.draketalley.kramer.sqldelight.GithubUserQueries

object GithubUserRepo {
    val queries: GithubUserQueries = KramerDb(DbConn.driver)
        .githubUserQueries

    fun fetchAll() {
       queries.select().executeAsList()
    }

    fun create(data: GithubUser) {
        queries.insert(data)
    }
}
