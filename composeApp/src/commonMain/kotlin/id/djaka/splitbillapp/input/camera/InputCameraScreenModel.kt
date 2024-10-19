package id.djaka.splitbillapp.input.camera

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import id.djaka.splitbillapp.service.bill.BillModel
import id.djaka.splitbillapp.service.bill.BillRepository
import id.djaka.splitbillapp.service.firebase.FirebaseService
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class InputCameraScreenModel(
    private val billRepository: BillRepository,
    private val firebaseService: FirebaseService
) : ScreenModel {
    var id = ""

    fun onCreate() {
        id = "DRAFT-${firebaseService.user?.uid ?: Uuid.random().toHexString()}"
        screenModelScope.launch {
            billRepository.saveBill(
                id,
                BillModel(
                    id = id,
                    items = emptyList(),
                    members = emptyList(),
                    ownerServerId = Firebase.auth.currentUser?.uid
                )
            )
        }
    }
}