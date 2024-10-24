package id.djaka.splitbillapp.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import id.djaka.splitbillapp.input.camera.CameraView
import id.djaka.splitbillapp.input.camera.TextRecognitionState
import id.djaka.splitbillapp.platform.Spacing
import id.djaka.splitbillapp.service.analyzer.TextRecognitionAnalyzer

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TextRecognitionScreen(state: TextRecognitionState) {
    val context = LocalContext.current
    val cameraPermissionState =
        rememberPermissionState(permission = android.Manifest.permission.CAMERA)

    var extractedText by remember { mutableStateOf("") }
    val analyzer = remember { TextRecognitionAnalyzer { extractedText = it } }
    LaunchedEffect(state) {
        state.onStartScan = {
            analyzer.startAnalyze {
                state.onFinishedScan(it)
            }
        }
    }
    Box {
        if (cameraPermissionState.status.isGranted) {
            CameraView(
                context = context,
                lifecycleOwner = LocalLifecycleOwner.current,
                analyzer = analyzer,
            )
        } else {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(Spacing.m),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                    "The camera is important for this app. Please grant the permission."
                } else {
                    "Camera permission required for this feature to be available. " +
                            "Please grant the permission"
                }
                Text(textToShow, textAlign = TextAlign.Center)
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Request permission")
                }
            }
        }
    }
}