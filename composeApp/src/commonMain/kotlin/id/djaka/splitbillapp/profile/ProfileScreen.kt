package id.djaka.splitbillapp.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import id.djaka.splitbillapp.platform.CoreTheme
import id.djaka.splitbillapp.platform.Spacing
import id.djaka.splitbillapp.widget.LoadingDialog
import kotlinx.coroutines.launch

class ProfileScreen : Screen {
    @Composable
    override fun Content() {
        val user = Firebase.auth.currentUser
        val coroutineScope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow
        CoreTheme {
            var isLoading by remember { mutableStateOf(false) }
            var showLogoutDialog by remember { mutableStateOf(false) }

            if (isLoading) {
                LoadingDialog { isLoading = false }
            }

            if (showLogoutDialog) {
                LogoutConfirmationDialog(
                    onConfirm = {
                        showLogoutDialog = false
                        coroutineScope.launch {
                            isLoading = true
                            Firebase.auth.signOut()
                            isLoading = false
                            navigator.popUntilRoot()
                        }
                    },
                    onDismiss = {
                        showLogoutDialog = false
                    }
                )
            }

            ProfileWidget(
                name = user?.displayName ?: "Unknown",
                email = user?.email ?: "Unknown",
                onLogout = {
                    showLogoutDialog = true
                }
            )
        }
    }
}

@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Logout") },
        text = { Text("Are you sure you want to logout? You will need to sign in again to access your account.") },
        confirmButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Logout")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileWidget(
    name: String,
    email: String,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") }
            )
        }
    ) {
        Column(
            Modifier.fillMaxSize()
                .padding(it)
                .padding(horizontal = Spacing.m),
            verticalArrangement = Arrangement.spacedBy(Spacing.m)
        ) {
            Text("Name: $name")
            Text("Email: $email")
            OutlinedButton(onClick = { onLogout() }, modifier = Modifier.fillMaxWidth()) {
                Text("Logout")
            }
        }
    }
}