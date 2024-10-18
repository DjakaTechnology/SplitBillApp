package id.djaka.splitbillapp

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import id.djaka.splitbillapp.di.appModule
import id.djaka.splitbillapp.di.screenModelModule
import id.djaka.splitbillapp.service.datastore.DataStoreStorage
import id.djaka.splitbillapp.service.datastore.createDataStore
import id.djaka.splitbillapp.service.datastore.dataStoreFileName
import org.koin.core.context.startKoin

fun main() = application {
    val isInitialized = remember {
        DataStoreStorage.dataStore = createDataStore {
            java.nio.file.Paths.get("").toAbsolutePath().toString() + dataStoreFileName
        }
        startKoin {
            modules(appModule)
            modules(screenModelModule)
        }
        true
    }

    if (!isInitialized) return@application
    Window(
        onCloseRequest = ::exitApplication,
        title = "SplitBillApp",
    ) {
        App()
    }
}