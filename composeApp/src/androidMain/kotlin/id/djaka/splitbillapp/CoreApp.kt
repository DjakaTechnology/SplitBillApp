package id.djaka.splitbillapp

import android.app.Application
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import id.djaka.splitbillapp.di.appModule
import id.djaka.splitbillapp.di.screenModelModule
import id.djaka.splitbillapp.service.datastore.DataStoreStorage
import id.djaka.splitbillapp.service.datastore.createDataStore
import org.koin.core.context.startKoin

class CoreApp: Application() {
    override fun onCreate() {
        super.onCreate()

        DataStoreStorage.dataStore = createDataStore(this)
        GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = WebClientId))

        startKoin {
            modules(appModule)
            modules(screenModelModule)
        }
    }
}