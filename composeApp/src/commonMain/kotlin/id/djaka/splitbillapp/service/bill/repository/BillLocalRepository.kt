package id.djaka.splitbillapp.service.bill.repository

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import id.djaka.splitbillapp.service.bill.BillModel
import id.djaka.splitbillapp.service.coreJson
import id.djaka.splitbillapp.service.datastore.DataStoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString

class BillLocalRepository(
    private val dataStoreService: DataStoreService
) : BillRepositoryContract {
    private val billsKey = stringPreferencesKey("bills")

    override val billsData: Flow<Map<String, BillModel>> by lazy {
        dataStoreService.getDataStore().data.map {
            coreJson.decodeFromString(it[billsKey] ?: "{}")
        }
    }

    override suspend fun saveBill(id: String, bill: BillModel) {
        dataStoreService.getDataStore().edit { preferences ->
            val data: MutableMap<String, BillModel> =
                coreJson.decodeFromString(preferences[billsKey] ?: "{}")
            data[id] = bill
            preferences[billsKey] = coreJson.encodeToString(data)
        }
    }

    override suspend fun deleteBill(id: String) {
        val edit = dataStoreService.getDataStore().edit { preferences ->
            val data: MutableMap<String, BillModel> =
                coreJson.decodeFromString(preferences[billsKey] ?: "{}")
            data.remove(id)
            preferences[billsKey] = coreJson.encodeToString(data)
        }
    }
}