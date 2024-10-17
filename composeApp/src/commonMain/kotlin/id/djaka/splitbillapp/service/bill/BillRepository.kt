package id.djaka.splitbillapp.service.bill

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
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
    private val draftBill = stringPreferencesKey("draftBill")

    val draftBillData: Flow<BillModel?> by lazy {
        datastoreService.getDataStore().data.map {
            it[draftBill]?.let { coreJson.decodeFromString(it) }
        }
    }

    val billsData: Flow<List<BillModel>> by lazy {
        datastoreService.getDataStore().data.map {
            coreJson.decodeFromString(it[billsKey] ?: "[]")
        }
    }

    suspend fun saveBills(bills: List<BillModel>) {
        datastoreService.getDataStore().edit { preferences ->
            preferences[billsKey] = coreJson.encodeToString(bills)
        }
    }

    suspend fun saveDraftBill(bill: BillModel) {
        datastoreService.getDataStore().edit { preferences ->
            preferences[draftBill] = coreJson.encodeToString(bill)
        }
    }
}