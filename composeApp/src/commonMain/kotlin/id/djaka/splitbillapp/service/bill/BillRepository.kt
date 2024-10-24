package id.djaka.splitbillapp.service.bill

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import id.djaka.splitbillapp.service.bill.repository.BillFirebaseRepository
import id.djaka.splitbillapp.service.bill.repository.BillLocalRepository
import id.djaka.splitbillapp.service.bill.repository.BillRepositoryContract
import id.djaka.splitbillapp.service.firebase.FirebaseService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat

@OptIn(ExperimentalCoroutinesApi::class)
class BillRepository(
    private val billFirebaseRepository: BillFirebaseRepository,
    private val billLocalRepository: BillLocalRepository,
    private val firebaseService: FirebaseService
) : BillRepositoryContract {
    private val isLoggedIn
        get() = Firebase.auth.currentUser != null

    private val repo
        get() = if (isLoggedIn) billFirebaseRepository else billLocalRepository

    override val billsData: Flow<Map<String, BillModel>> by lazy {
        firebaseService.userFlow.flatMapConcat {
            repo.billsData
        }
    }

    override fun getBillFlow(id: String): Flow<BillModel?> {
        return firebaseService.userFlow.flatMapConcat {
            repo.getBillFlow(id)
        }
    }

    override suspend fun saveBill(id: String, bill: BillModel) {
        repo.saveBill(id, bill)
    }

    override suspend fun deleteBill(id: String) {
        repo.deleteBill(id)
    }
}