package id.djaka.splitbillapp.service.bill

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import id.djaka.splitbillapp.service.coreJson
import id.djaka.splitbillapp.service.datastore.DataStoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString

// Should've been using SQLite but KMP doesn't properly support it
class BillRepository(
    private val datastoreService: DataStoreService
) {
    private val billsKey = stringPreferencesKey("bills")

    val billsData: Flow<Map<String, BillModel>> by lazy {
        datastoreService.getDataStore().data.map {
            coreJson.decodeFromString(it[billsKey] ?: "{}")
        }
    }

    suspend fun saveBill(id: String, bill: BillModel) {
        datastoreService.getDataStore().edit { preferences ->
            val data: MutableMap<String, BillModel> =
                coreJson.decodeFromString(preferences[billsKey] ?: "{}")
            data[id] = bill
            preferences[billsKey] = coreJson.encodeToString(data)
        }

        Firebase.firestore.collection("bills").document(id).set(
            BillModel.serializer(),
            bill
        ) {
            encodeDefaults = false
        }
    }

    suspend fun deleteBill(id: String) {
        datastoreService.getDataStore().edit { preferences ->
            val data: MutableMap<String, BillModel> =
                coreJson.decodeFromString(preferences[billsKey] ?: "{}")
            data.remove(id)
            preferences[billsKey] = coreJson.encodeToString(data)
        }
    }
}