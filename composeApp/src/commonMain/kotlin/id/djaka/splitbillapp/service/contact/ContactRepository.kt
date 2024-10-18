package id.djaka.splitbillapp.service.contact

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import id.djaka.splitbillapp.service.coreJson
import id.djaka.splitbillapp.service.datastore.DataStoreService
import id.djaka.splitbillapp.service.safeDecodeFromString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString

class ContactRepository(
    val dataStoreService: DataStoreService
) {
    val contactKey = stringPreferencesKey("contacts")

    val contactData: Flow<Map<String, ContactModel>> = dataStoreService.getDataStore().data.map {
        coreJson.safeDecodeFromString(it[contactKey] ?: "{}") ?: emptyMap()
    }

    suspend fun saveContactData(data: ContactModel) {
        dataStoreService.getDataStore().edit { preferences ->
            val contactData: MutableMap<String, ContactModel> =
                coreJson.safeDecodeFromString(preferences[contactKey] ?: "{}") ?: mutableMapOf()
            contactData[data.id] = data
            preferences[contactKey] = coreJson.encodeToString(contactData)
        }
    }
}