package id.djaka.splitbillapp.input.camera

import android.util.Log
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.asErr
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import id.djaka.splitbillapp.input.item.InputItemsScreen
import id.djaka.splitbillapp.service.bill.BillModel
import id.djaka.splitbillapp.service.bill.BillRepository
import id.djaka.splitbillapp.service.firebase.FirebaseService
import id.djaka.splitbillapp.service.recognition.TextRecognitionService
import id.djaka.splitbillapp.util.isInternetAvailable
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class InputCameraScreenModel(
    private val billRepository: BillRepository,
    private val firebaseService: FirebaseService,
    private val textRecognitionService: TextRecognitionService,
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

    suspend fun onProcessCameraText(text: String, navigator: Navigator): Result<Unit, String> {
        if (!isInternetAvailable()) return Err("Internet unavailable, please check your internet connection")
        if (text.isNotEmpty()) {
            val response = runCatching {
                textRecognitionService.recognizeTextToModel(text)
            }.mapError { e ->
                if (e.message?.contains("network error", ignoreCase = true) == true) {
                    "Network error. Please check your internet connection."
                } else {
                    "Failed to recognize text, try again later."
                }
            }
            if (response.isErr) return Err(response.error)
            val result = response.value
            val currentData = billRepository.getBillFlow(id).filterNotNull().first()
            billRepository.saveBill(
                id, currentData.copy(
                    items = result.items.map {
                        BillModel.Item(
                            id = Uuid.random().toHexString(),
                            name = it.name,
                            price = it.price ?: it.total?.let { total ->
                                total / it.qty
                            } ?: 0.0,
                            qty = it.qty,
                            memberIds = emptySet()
                        )
                    },
                    feeItems = result.fee.map {
                        BillModel.FeeItem(
                            id = Uuid.random().toHexString(),
                            name = it.name,
                            price = it.price ?: it.discount?.let { -it } ?: 0.0
                        )
                    }
                ))
        }
        navigator.push(
            InputItemsScreen(id)
        )

        return Ok(Unit)
    }
}