package id.djaka.splitbillapp.input.camera

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.remoteConfig
import id.djaka.splitbillapp.input.item.InputItemsScreen
import id.djaka.splitbillapp.platform.CoreTheme
import id.djaka.splitbillapp.platform.Spacing
import id.djaka.splitbillapp.widget.LoadingDialog
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class InputCameraScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<InputCameraScreenModel>()
        val coroutineScope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        LaunchedEffect(screenModel) {
            screenModel.onCreate()
            if (!Firebase.remoteConfig.getValue("cloudVision").asBoolean()) {
                navigator.pop()
                navigator.push(
                    InputItemsScreen(screenModel.id)
                )
            }
        }
        CoreTheme {
            var isLoading by remember { mutableStateOf(false) }
            if (isLoading) {
                LoadingDialog("Processing image, please wait...")
            }

            InputCameraWidget(
                snackbarHostState,
                onOpenManual = {
                    navigator.push(
                        InputItemsScreen(screenModel.id)
                    )
                },
                onClickBack = {
                    navigator.pop()
                },
                onScan = {
                    coroutineScope.launch {
                        isLoading = true
                        try {
                            val result = screenModel.onProcessCameraText(it, navigator)
                            isLoading = false
                            if (result.isErr) snackbarHostState.showSnackbar(result.error)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            isLoading = false
                            snackbarHostState.showSnackbar("Something went wrong, please try again later")
                        }
                    }
                },
                onFailedScan = {
                    coroutineScope.launch {
                        if (it.message == "Cloud Vision batchAnnotateImages call failure") {
                            snackbarHostState.showSnackbar("Internet unavailable, please check your internet connection")
                        } else {
                            snackbarHostState.showSnackbar("Something went wrong, please try again later")
                        }
                        it.printStackTrace()
                        isLoading = false
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputCameraWidget(
    snackbarHostState: SnackbarHostState,
    onOpenManual: () -> Unit = {},
    onClickBack: () -> Unit = {},
    onScan: (String) -> Unit = {},
    onFailedScan: (e: Exception) -> Unit = {}
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                },
                navigationIcon = {
                    IconButton(
                        onClick = onClickBack,
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "back",
                        )
                    }
                }
            )
        }
    ) {
        var isLoading by remember { mutableStateOf(false) }
        if (isLoading) {
            LoadingDialog("Processing image, please wait...")
        }
        val recognitionState = rememberTextRecognitionState(
            onFinishScan = {
                onScan(it)
                isLoading = false
            },
            onFailedScan = {
                isLoading = false
                onFailedScan(it)
            }
        )
        Column(Modifier.fillMaxSize().padding(it)) {
            Spacer(Modifier.width(Spacing.m))
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                createTextRecognitionScreen(state = recognitionState)
                Box(
                    Modifier.fillMaxWidth()
                        .background(
                            Color.Black.copy(alpha = 0.5f)
                        )
                        .align(Alignment.BottomCenter),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth(0.6f)
                            .padding(Spacing.l),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {},
                        ) {
                            Icon(
                                Icons.Filled.Image,
                                "pick image",
                            )
                        }

                        IconButton(
                            onClick = {
                                recognitionState.onStartScan()
                                isLoading = true
                            },
                            modifier = Modifier
                                .border(1.dp, Color.White, CircleShape)
                        ) {
                            Icon(
                                Icons.Filled.Circle,
                                "take picture",
                                modifier = Modifier.size(48.dp),
                            )
                        }

                        IconButton(
                            onClick = {
                                onOpenManual()
                            },
                        ) {
                            Icon(
                                Icons.Filled.Edit,
                                "manual",
                            )
                        }
                    }
                }
            }
        }
    }
}