package views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.runBlocking
import service.KramerService

@Composable
fun IndexView() {
   return Column(Modifier.fillMaxSize(), Arrangement.SpaceBetween) {
      Text(text = "Welcome!")
      Button(
              onClick = {
                  runBlocking {
                      val token = KramerService.getToken()
                              .await()
                      println(token)
                  }
              }
      ) {
          Text(text = "Sign Up With Github")
      }
   }
}