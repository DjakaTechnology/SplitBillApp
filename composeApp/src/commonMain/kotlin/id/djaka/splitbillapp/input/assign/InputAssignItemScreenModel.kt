package id.djaka.splitbillapp.input.assign

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import id.djaka.splitbillapp.service.bill.BillModel
import id.djaka.splitbillapp.service.bill.BillRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class InputAssignItemScreenModel(
    val billRepository: BillRepository
) : ScreenModel {

    val memberItem = mutableStateListOf<MemberItem>()
    val menuItem = mutableStateListOf<MenuItem>()
    val feeItem = mutableStateListOf<FeeItem>()
    val total = snapshotFlow {
        menuItem.sumOf { it.total } + feeItem.sumOf { it.price }
    }

    var currentSelectedMember by mutableStateOf<String?>(null)

    fun onCreate() {
        memberItem.add(
            MemberItem(
                id = "YOU",
                name = "You",
            ),
        )

        screenModelScope.launch {
            val data = billRepository.draftBillData.firstOrNull() ?: return@launch
            menuItem.addAll(
                data.items.map { item ->
                    MenuItem(
                        id = item.id,
                        name = item.name,
                        qty = item.qty,
                        price = item.price,
                        memberIds = data.members.map { it.id }.toSet(),
                    )
                }
            )
            memberItem.addAll(
                data.members.filter { it.id != "YOU" }.map { member ->
                    MemberItem(
                        id = member.id,
                        name = member.name,
                    )
                }
            )
            feeItem.addAll(
                data.feeItems.map { feeItem ->
                    FeeItem(
                        id = feeItem.id,
                        name = feeItem.name,
                        price = feeItem.price,
                    )
                }
            )
        }
    }

    private var autoSaveJob: Job? = null
    private fun triggerAutoSave() {
        autoSaveJob?.cancel()
        autoSaveJob = screenModelScope.launch {
            val data = billRepository.draftBillData.firstOrNull() ?: return@launch
            billRepository.saveDraftBill(
                data.copy(
                    items = menuItem.map { item ->
                        BillModel.Item(
                            id = item.id,
                            name = item.name,
                            qty = item.qty,
                            price = item.price,
                            memberIds = item.memberIds,
                        )
                    },
                    members = memberItem.map { member ->
                        BillModel.Member(
                            id = member.id,
                            name = member.name,
                        )
                    },
                )
            )
        }
    }

    fun toggleAssignMember(index: Int, memberId: String) {
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

    fun removeMember(index: Int) {
        if (currentSelectedMember == memberItem[index].id) {
            currentSelectedMember = null
        }
        memberItem.removeAt(index)
        menuItem.forEachIndexed { i, item ->
            val memberIds = item.memberIds.toMutableSet()
            memberIds.remove(memberItem[index].id)
            menuItem[i] = item.copy(memberIds = memberIds)
        }

        triggerAutoSave()
    }

    fun selectMember(it: Int) {
        currentSelectedMember = memberItem[it].id
    }

    fun addNewMember(it: String) {
        memberItem.add(
            MemberItem(
                id = Uuid.random().toHexString(),
                name = it,
            ),
        )

        triggerAutoSave()
    }

    data class MemberItem(
        val id: String,
        val name: String,
    )

    data class FeeItem(
        val id: String,
        val name: String,
        val price: Double,
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