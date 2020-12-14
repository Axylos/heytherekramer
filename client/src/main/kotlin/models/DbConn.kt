package models

import com.draketalley.kramer.KramerDb
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import java.io.File

object DbConn {
    val driver: SqlDriver

    init {
        val home = System.getProperty("user.home")

        val path = "$home/Documents/kramer.db"
        File(path).createNewFile()
        driver = JdbcSqliteDriver("jdbc:sqlite:$path")
    }

    fun buildDb() {
        KramerDb.Schema.create(driver)
    }
}