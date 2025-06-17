package id.djaka.splitbillapp

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.android
import dev.gitlive.firebase.firestore.firestore
import id.djaka.splitbillapp.di.appModule
import id.djaka.splitbillapp.di.screenModelModule
import id.djaka.splitbillapp.service.datastore.DataStoreStorage
import id.djaka.splitbillapp.service.datastore.createDataStore
import org.koin.core.context.startKoin

lateinit var app: CoreApp

class CoreApp : Application() {
    override fun onCreate() {
        super.onCreate()

        app = this
        DataStoreStorage.dataStore = createDataStore(this)
        GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = WebClientId))

//        Firebase.firestore.android.firestoreSettings =
//            FirebaseFirestoreSettings.Builder(Firebase.firestore.android.firestoreSettings)
//                .setLocalCacheSettings(memoryCacheSettings { })
//                .build()

        startKoin {
            modules(appModule)
            modules(screenModelModule)
        }
    }
}