package id.djaka.splitbillapp.input.assign

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import id.djaka.splitbillapp.service.bill.BillModel
import id.djaka.splitbillapp.service.bill.BillRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class InputAssignItemScreenModel(
    val billRepository: BillRepository
): ScreenModel {

    val memberItem = mutableStateListOf<MemberItem>()
    val menuItem = mutableStateListOf<MenuItem>()

    var currentSelectedMember by mutableStateOf<String?>(null)

    fun onCreate() {
        memberItem.add(
            MemberItem(
                id = "YOU",
                name = "You",
            ),
        )
        memberItem.add(
            MemberItem(
                id = "FRIEND",
                name = "Friend",
            ),
        )

        screenModelScope.launch {
            val data = billRepository.draftBillData.firstOrNull() ?: return@launch
            menuItem.addAll(data.items.map { item ->
                MenuItem(
                    id = item.id,
                    name = item.name,
                    qty = item.qty,
                    price = item.price,
                    memberIds = data.members,
                )
            })
        }
    }

    private var autoSaveJob: Job? = null
    private fun triggerAutoSave() {
        autoSaveJob?.cancel()
        autoSaveJob = screenModelScope.launch {
            val data = billRepository.draftBillData.firstOrNull() ?: return@launch
            billRepository.saveDraftBill(data.copy(
                items = menuItem.map { item ->
                    BillModel.Item(
                        id = item.id,
                        name = item.name,
                        qty = item.qty,
                        price = item.price,
                        memberIds = item.memberIds,
                    )
                },
            ))
        }
    }

    fun assignMember(index: Int, memberId: String) {
        val item = menuItem[index]
        val memberIds = item.memberIds.toMutableSet()
        if (memberId in memberIds) {
            memberIds.remove(memberId)
        } else {
            memberIds.add(memberId)
        }
        menuItem[index] = item.copy(memberIds = memberIds)

        triggerAutoSave()
    }

    data class MemberItem(
        val id: String,
        val name: String,
    )

    data class MenuItem(
        val id: String,
        val name: String,
        val qty: Int,
        val price: Double,
        val memberIds: Set<String>,
    ) {
        val total = price * qty
        val pricePerMember = total / memberIds.size
    }
}