package id.djaka.splitbillapp.service.bill.repository

import id.djaka.splitbillapp.service.bill.BillModel
import kotlinx.coroutines.flow.Flow

interface BillRepositoryContract {
    val billsData: Flow<Map<String, BillModel>>

    suspend fun saveBill(id: String, bill: BillModel)

    suspend fun deleteBill(id: String)
}