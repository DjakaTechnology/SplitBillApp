package id.djaka.splitbillapp.service.bill.repository

import android.util.Log
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.firestore
import id.djaka.splitbillapp.service.bill.BillModel
import id.djaka.splitbillapp.service.firebase.FirebaseService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

private const val KEY = "bills"

@OptIn(ExperimentalCoroutinesApi::class)
class BillFirebaseRepository(
    private val firebaseService: FirebaseService
): BillRepositoryContract {
    override val billsData: Flow<Map<String, BillModel>>
        get() = firebaseService.userFlow.filterNotNull().flatMapConcat {
            getCollection().where {
                "ownerServerId" equalTo it.uid
            }.snapshots.map {
                it.documents.map { it.data(BillModel.serializer()) }.associateBy { it.id }
            }
        }

    override fun getBillFlow(id: String): Flow<BillModel?> {
        Log.d("DJAKAAA", id)
        return getCollection().document(id).snapshots.map {
            it.data(BillModel.serializer())
        }
    }

    override suspend fun saveBill(id: String, bill: BillModel) {
        getCollection().document(id).set(bill)
    }

    private fun getCollection(): CollectionReference {
        return Firebase.firestore.collection(KEY)
    }

    override suspend fun deleteBill(id: String) {
        getCollection().document(id).delete()
    }
}