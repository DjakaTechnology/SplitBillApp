package id.djaka.splitbillapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import id.djaka.splitbillapp.di.appModule
import id.djaka.splitbillapp.di.screenModelModule
import id.djaka.splitbillapp.platform.LocalDarkColorScheme
import id.djaka.splitbillapp.platform.LocalLightColorScheme
import id.djaka.splitbillapp.service.datastore.DataStoreStorage
import id.djaka.splitbillapp.service.datastore.createDataStore
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataStoreStorage.dataStore = createDataStore(this)
        GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = WebClientId))
        startKoin {
            modules(appModule)
            modules(screenModelModule)
        }

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
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}