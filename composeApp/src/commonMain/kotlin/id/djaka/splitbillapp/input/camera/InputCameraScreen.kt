package id.djaka.splitbillapp.input.camera

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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import id.djaka.splitbillapp.input.item.InputItemsScreen
import id.djaka.splitbillapp.platform.CoreTheme
import id.djaka.splitbillapp.platform.Spacing
import kotlinx.coroutines.launch

class InputCameraScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<InputCameraScreenModel>()
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(screenModel) {
            screenModel.onCreate()
        }
        CoreTheme {
            var isLoading by remember { mutableStateOf(false) }
            if (isLoading) {
                Dialog(onDismissRequest = {
                    isLoading = false
                }) {
                    Card {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Spacing.m),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(Spacing.m)
                        ) {
                            CircularProgressIndicator()
                            Text("Loading...")
                        }
                    }
                }
            }

            InputCameraWidget(
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
                            screenModel.onProcessCameraText(it, navigator)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
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
    onOpenManual: () -> Unit = {},
    onClickBack: () -> Unit = {},
    onScan: (String) -> Unit = {},
) {
    Scaffold(
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
        val recognitionState = rememberTextRecognitionState(
            onFinishScan = {
                onScan(it)
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