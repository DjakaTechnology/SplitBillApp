package id.djaka.splitbillapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.registry.screenModule
import id.djaka.splitbillapp.di.appModule
import id.djaka.splitbillapp.di.screenModelModule
import id.djaka.splitbillapp.service.datastore.DataStoreStorage
import id.djaka.splitbillapp.service.datastore.createDataStore
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataStoreStorage.dataStore = createDataStore(this)
        startKoin {
            modules(appModule)
            modules(screenModelModule)
        }

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}