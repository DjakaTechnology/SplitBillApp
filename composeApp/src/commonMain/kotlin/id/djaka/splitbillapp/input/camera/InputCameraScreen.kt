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
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import id.djaka.splitbillapp.input.item.InputItemsScreen
import id.djaka.splitbillapp.platform.CoreTheme
import id.djaka.splitbillapp.platform.Spacing

class InputCameraScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<InputCameraScreenModel>()
        LaunchedEffect(screenModel) {
            screenModel.onCreate()
        }
        CoreTheme {
            InputCameraWidget(
                onClickCamera = {
                    navigator.push(
                        InputItemsScreen()
                    )
                }
            )
        }
    }
}

@Composable
fun InputCameraWidget(
    onClickCamera: () -> Unit = {}
) {
    Scaffold {
        Column(Modifier.fillMaxSize()) {
            Spacer(Modifier.width(Spacing.m))
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Green)
            ) {
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
                            onClick = onClickCamera,
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
                            onClick = {},
                        ) {
                            Icon(
                                Icons.Filled.Edit,
                                "take picture",
                            )
                        }
                    }
                }
            }
        }
    }
}