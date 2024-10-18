package id.djaka.splitbillapp.home

import cafe.adriel.voyager.core.model.ScreenModel
import id.djaka.splitbillapp.service.bill.BillRepository
import kotlinx.coroutines.flow.map

class HomeScreenModel(
    private val repository: BillRepository
): ScreenModel {
    val bills = repository.billsData.map {
        it.values.toList()
    }

    fun onCreate() {
    }
}