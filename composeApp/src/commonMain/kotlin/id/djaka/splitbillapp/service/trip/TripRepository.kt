package id.djaka.splitbillapp.service.trip

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import id.djaka.splitbillapp.service.coreJson
import id.djaka.splitbillapp.service.datastore.DataStoreService
import id.djaka.splitbillapp.service.safeDecodeFromString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString

class TripRepository(
    private val dataStoreService: DataStoreService
) {
    private val tripKey = stringPreferencesKey("trip")

    val tripData: Flow<Map<String, TripModel>> by lazy {
        dataStoreService.getDataStore().data.map {
            coreJson.safeDecodeFromString<Map<String, TripModel>>(it[tripKey] ?: "{}").orEmpty()
        }
    }

    suspend fun saveTripData(tripModel: TripModel) {
        dataStoreService.getDataStore().edit {
            val tripData =
                coreJson.safeDecodeFromString<Map<String, TripModel>>(it[tripKey] ?: "{}")
                    .orEmpty()
                    .toMutableMap()
            tripData[tripModel.id] = tripModel
            it[tripKey] = coreJson.encodeToString(tripData)
        }
        Firebase.firestore.collection("trip").document(tripModel.id).set(TripModel.serializer(), tripModel)
    }

    suspend fun deleteTripData(tripId: String) {
        dataStoreService.getDataStore().edit {
            val tripData =
                coreJson.safeDecodeFromString<Map<String, TripModel>>(it[tripKey] ?: "{}")
                    .orEmpty()
                    .toMutableMap()
            tripData.remove(tripId)
            it[tripKey] = coreJson.encodeToString(tripData)
        }
        Firebase.firestore.collection("trip").document(tripId).delete()
    }
}