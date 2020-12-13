import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.draketalley.kramer.KramerDb
import models.DbConn
import views.UserForm

fun main(args: Array<String>) {
    if (args.any()) {
        if (args.get(0).equals("migrate"))  {
            println("migrate: ${KramerDb.Schema.version}")
            KramerDb.Schema.migrate(DbConn.driver, 0, KramerDb.Schema.version)
        } else if (args.get(0).equals("init")) {
            println("init")
            KramerDb.Schema.create(DbConn.driver)
        }

    } else {
        println("run")
        runner()
    }
}

@Composable
fun App() {
    return Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
        Text(text = "hey there")
        UserForm()
    }
}

fun runner() = Window(title = "Kramer Notifications", IntSize(500, 500)) {
    MaterialTheme { App() }
}
