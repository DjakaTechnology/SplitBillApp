package id.djaka.splitbillapp.input.item

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshotFlow
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import id.djaka.splitbillapp.service.bill.BillModel
import id.djaka.splitbillapp.service.bill.BillRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class InputItemScreenModel(
    val billRepository: BillRepository,
) : ScreenModel {
    var id: String = ""
    val menuItems = mutableStateListOf<MenuItem>()
    val feeItem = mutableStateListOf<FeeItem>()

    val total = snapshotFlow {
        menuItems.sumOf {
            it.total.toDoubleOrNull() ?: it.totalAutoFill
        } + feeItem.sumOf { it.price.toDoubleOrNull() ?: 0.0 }
    }

    fun onCreate() {
        addMenuItem()
        feeItem.add(
            FeeItem(
                id = Uuid.random().toHexString(),
                name = "Discount",
                price = "-0"
            ),
        )
        feeItem.add(
            FeeItem(
                id = Uuid.random().toHexString(),
                name = "Tax",
                price = "0"
            )
        )
    }

    private var autoSaveJob: Job? = null
    private fun triggerAutoSave() {
        autoSaveJob?.cancel()
        autoSaveJob = screenModelScope.launch {
            delay(200)
            val existingBill = billRepository.billsData.first()[id]
                ?: throw IllegalStateException("Draft bill not found")

            val existingItems = existingBill.items.associateBy { it.id }
            billRepository.saveBill(
                id,
                existingBill.copy(
                    items = menuItems.map {
                        BillModel.Item(
                            name = it.name,
                            price = it.price.toDoubleOrNull() ?: it.priceAutoFill,
                            qty = it.qty.toIntOrNull() ?: 0,
                            id = it.id,
                            memberIds = existingItems[it.id]?.memberIds ?: emptySet()
                        )
                    },
                    feeItems = feeItem.map {
                        BillModel.FeeItem(
                            name = it.name,
                            price = it.price.toDoubleOrNull() ?: 0.0,
                            id = it.id
                        )
                    }
                )
            )
        }
    }

    fun addMenuItem() {
        menuItems.add(
            MenuItem(
                id = Uuid.random().toHexString(),
                name = "",
                price = "",
                qty = "",
                total = ""
            )
        )
    }

    fun addFeeItem() {
        feeItem.add(
            FeeItem(
                id = Uuid.random().toHexString(),
                name = "",
                price = ""
            )
        )
    }

    fun onHandleMenuItemChange(
        index: Int,
        name: String,
        price: String,
        qty: String,
        total: String
    ) {
        val qtyInt = qty.toIntOrNull()
        menuItems[index] = menuItems[index].copy(
            name = name,
            price = price,
            qty = qty,
            total = total,
            priceAutoFill = if (qtyInt != null) (total.toDoubleOrNull() ?: 0.0) / qtyInt else 0.0,
            totalAutoFill = if (qtyInt != null) (price.toDoubleOrNull() ?: 0.0) * qtyInt else 0.0
        )
        triggerAutoSave()
    }

    data class FeeItem(
        val id: String,
        val name: String,
        val price: String,
    )

    data class MenuItem(
        val id: String,
        val name: String,
        val price: String,
        val qty: String,
        val total: String,
        val priceAutoFill: Double = 0.0,
        val totalAutoFill: Double = 0.0
    )
}