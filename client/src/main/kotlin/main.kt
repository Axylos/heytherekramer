import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.draketalley.kramer.KramerDb
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import models.DbConn
import models.GithubUserRepo
import models.Route
import service.GithubService
import views.IndexView
import views.UserForm
import java.net.URI
import java.net.URL

fun main(args: Array<String>) = runBlocking {
    val url = URI("https://draketalley.com");
//    java.awt.Desktop.getDesktop().browse(url)
    if (args.any()) {
        if (args.get(0).equals("migrate"))  {
            println("migrate: ${KramerDb.Schema.version}")
            KramerDb.Schema.migrate(DbConn.driver, 0, KramerDb.Schema.version)
        } else if (args.get(0).equals("init")) {
            println("init")
            KramerDb.Schema.create(DbConn.driver)
        } else if (args.get(0).equals("reset")) {
            GithubUserRepo.reset()
        }

    } else {
        println("run")
        runner()
    }
}

@Composable
fun App() {
    val route = remember {
        val currentRoute = when(GithubUserRepo.hasToken()) {
            true -> {
                GlobalScope.launch {
                    GithubService.getUser()
                }
                Route.Index
            }
            false -> Route.GithubUserForm
        }

        mutableStateOf(currentRoute)
    }

    return Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
        when(route.value) {
            Route.GithubUserForm -> UserForm(onSubmit = {
                val id = GithubUserRepo.storeToken(it)
                GithubService.token = it
                route.value = Route.Index
            })
            Route.Index -> IndexView()
        }
    }
}

fun runner() = Window(title = "Kramer Notifications", IntSize(500, 500)) {
    MaterialTheme { App() }
}
