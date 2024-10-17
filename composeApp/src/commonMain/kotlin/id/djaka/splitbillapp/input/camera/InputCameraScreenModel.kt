package id.djaka.splitbillapp.input.camera

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import id.djaka.splitbillapp.service.bill.BillModel
import id.djaka.splitbillapp.service.bill.BillRepository
import kotlinx.coroutines.launch

class InputCameraScreenModel(
    private val billRepository: BillRepository,
): ScreenModel {
    fun onCreate() {
       screenModelScope.launch {
           billRepository.saveDraftBill(BillModel(
               items = emptyList(),
               members = emptyList()
           ))
       }
    }
}