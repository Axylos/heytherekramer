package views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun UserForm() {
    val tokenField = remember { mutableStateOf("")}
    return Column(Modifier.fillMaxSize(), Arrangement.SpaceAround) {
        Text(text = "Welcome")
        Text(text = tokenField.value)
        TextField(
            value = tokenField.value,
            singleLine = true,
            onValueChange = {
                tokenField.value = it.replace("\n", "")
            },
            label = { Text(text = "Github User Token")}
        )
    }
}