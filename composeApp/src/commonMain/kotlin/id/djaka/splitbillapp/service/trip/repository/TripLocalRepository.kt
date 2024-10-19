package id.djaka.splitbillapp.service.trip.repository

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import id.djaka.splitbillapp.service.coreJson
import id.djaka.splitbillapp.service.datastore.DataStoreService
import id.djaka.splitbillapp.service.safeDecodeFromString
import id.djaka.splitbillapp.service.trip.TripModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString

class TripLocalRepository(
    private val dataStoreService: DataStoreService
) : TripRepositoryContract {
    private val tripKey = stringPreferencesKey("trip")

    override val tripData: Flow<Map<String, TripModel>> by lazy {
        dataStoreService.getDataStore().data.map {
            coreJson.safeDecodeFromString<Map<String, TripModel>>(it[tripKey] ?: "{}").orEmpty()
        }
    }

    override suspend fun saveTripData(tripModel: TripModel) {
        dataStoreService.getDataStore().edit {
            val tripData =
                coreJson.safeDecodeFromString<Map<String, TripModel>>(it[tripKey] ?: "{}")
                    .orEmpty()
                    .toMutableMap()
            tripData[tripModel.id] = tripModel
            it[tripKey] = coreJson.encodeToString(tripData)
        }
    }

    override suspend fun deleteTripData(tripId: String) {
        dataStoreService.getDataStore().edit {
            val tripData =
                coreJson.safeDecodeFromString<Map<String, TripModel>>(it[tripKey] ?: "{}")
                    .orEmpty()
                    .toMutableMap()
            tripData.remove(tripId)
            it[tripKey] = coreJson.encodeToString(tripData)
        }
    }
}