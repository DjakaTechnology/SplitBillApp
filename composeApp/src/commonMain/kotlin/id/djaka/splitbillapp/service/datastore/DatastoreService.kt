package id.djaka.splitbillapp.service.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

object DataStoreStorage {
    lateinit var dataStore: DataStore<Preferences>
}

class DataStoreService {
    fun getDataStore() = DataStoreStorage.dataStore
}