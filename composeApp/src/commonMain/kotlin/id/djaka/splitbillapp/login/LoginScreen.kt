package id.djaka.splitbillapp.login

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    val snackbar = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbar)
        },
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
                    it.exceptionOrNull()?.printStackTrace()
                    val data = it.getOrNull()
                    if (it.isSuccess && data != null) {
                        Firebase.auth.updateCurrentUser(data)
                        navigator.pop()
                    } else if (it.exceptionOrNull()?.message?.contains("A network error") == true) {
                        snackbar.showSnackbar("Network error, please check your internet connection and try again")
                    } else if (it.exceptionOrNull()?.message == "Idtoken is null") {
                        snackbar.showSnackbar("Login Canceled")
                    } else {
                        snackbar.showSnackbar("Something went wrong, please try again later")
                    }
                }
            }, linkAccount = true) {
                GoogleSignInButton(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                    text = "Login with Google"
                ) {
                    this.onClick()
                }
            }
        }

    }
}