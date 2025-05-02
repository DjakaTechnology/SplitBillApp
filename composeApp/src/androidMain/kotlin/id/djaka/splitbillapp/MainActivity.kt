package id.djaka.splitbillapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.remoteConfig
import id.djaka.splitbillapp.platform.LocalDarkColorScheme
import id.djaka.splitbillapp.platform.LocalLightColorScheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                CompositionLocalProvider(
                    LocalLightColorScheme provides dynamicLightColorScheme(this),
                    LocalDarkColorScheme provides dynamicDarkColorScheme(this)
                ) {
                    App()
                }
            } else {
                App()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            Firebase.remoteConfig.fetch()
            Log.d("DJAKAA", Firebase.remoteConfig.fetchAndActivate().toString())
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}