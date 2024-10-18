package id.djaka.splitbillapp.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import id.djaka.splitbillapp.platform.CoreTheme
import id.djaka.splitbillapp.platform.Spacing
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        CoreTheme {
            LoginScreenWidget()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenWidget() {
    val navigator = LocalNavigator.currentOrThrow
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Login")
            })
        }
    ) {
        Box(
            Modifier.fillMaxSize().padding(it).padding(horizontal = Spacing.m),
            contentAlignment = Alignment.Center
        ) {
            val scope = rememberCoroutineScope()
            GoogleButtonUiContainerFirebase(onResult = {
                scope.launch {
                    val data = it.getOrNull()
                    if (it.isSuccess && data != null) {
                        Firebase.auth.updateCurrentUser(data)
                        navigator.pop()
                    }
                }
            }, linkAccount = true) {
                GoogleSignInButton(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Center)
                ) {
                    this.onClick()
                }
            }
        }

    }
}