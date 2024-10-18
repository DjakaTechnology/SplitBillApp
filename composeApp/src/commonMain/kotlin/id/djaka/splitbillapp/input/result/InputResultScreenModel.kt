package id.djaka.splitbillapp.input.result

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import id.djaka.splitbillapp.service.bill.BillModel
import id.djaka.splitbillapp.service.bill.BillRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class InputResultScreenModel(
    private val repository: BillRepository
) : ScreenModel {
    var id = ""
    val members = mutableStateListOf<Member>()

    var invoiceDetail by mutableStateOf(InvoiceDetail(emptyList(), emptyList()))
    val billData = repository.billsData.map { it[id] }

    fun onCreate(id: String) {
        this.id = id
        screenModelScope.launch {
            loadBills()
            loadInvoiceDetail()
        }
    }

    private suspend fun loadInvoiceDetail() {
        val data = repository.billsData.first().get(id) ?: return
        val membersMap = data.members.associateBy { it.id }
        val items = data.items.map {
            InvoiceDetail.Item(
                name = it.name,
                price = it.price,
                qty = it.qty,
                total = it.total,
                member = it.memberIds.map {
                    membersMap[it]?.name.orEmpty()
                }
            )
        }
        val fees = data.feeItems.map {
            InvoiceDetail.Fee(
                name = it.name,
                price = it.price
            )
        }
        invoiceDetail = InvoiceDetail(
            items = items,
            fees = fees
        )
    }

    private suspend fun loadBills() {
        val data = repository.billsData.firstOrNull()?.get(id) ?: return
        val membersData = data.members.map {
            Member(
                id = it.id,
                name = it.name,
                total = 0.0,
                isPaid = it.isPaid,
                menuItem = listOf(),
            )
        }.associateBy { it.id }.toMutableMap()

        data.items.forEach {
            val pricePerMember = it.total / it.memberIds.size
            it.memberIds.forEach { memberId ->
                val existingData = membersData[memberId] ?: return@forEach
                val items = existingData.menuItem.toMutableList()
                items.add(
                    Member.MenuItem(
                        name = it.name,
                        price = pricePerMember
                    )
                )
                membersData[memberId] = existingData.copy(
                    total = existingData.total + pricePerMember,
                    menuItem = items
                )
            }
        }
        data.feeItems.forEach {
            val pricePerMember = it.price / membersData.size
            membersData.values.forEach { member ->
                val items = member.menuItem.toMutableList()
                items.add(
                    Member.MenuItem(
                        name = it.name,
                        price = pricePerMember
                    )
                )
                membersData[member.id] = member.copy(
                    total = member.total + pricePerMember,
                    menuItem = items
                )
            }
        }

        members.addAll(
            membersData.values.sortedBy { it.name }
        )
    }

    fun setPaid(index: Int, isPaid: Boolean) {
        members[index] = members[index].copy(isPaid = isPaid)
        triggerAutoSaveJob()
    }

    private var autoSaveJob: Job? = null
    private fun triggerAutoSaveJob() {
        autoSaveJob = screenModelScope.launch {
            delay(500)
            val data = repository.billsData.firstOrNull()?.get(id) ?: return@launch
            repository.saveBill(id, data.copy(
                members = members.map {
                    BillModel.Member(
                        id = it.id,
                        name = it.name,
                        isPaid = it.isPaid
                    )
                }
            ))
        }
    }

    fun setDetailData(it: InputResultDetailWidgetState) {
        screenModelScope.launch {
            val data = billData.first() ?: return@launch
            repository.saveBill(id, data.copy(
                name = it.name,
                date = it.date,
            ))
        }
    }

    data class Member(
        val id: String,
        val name: String,
        val total: Double,
        val menuItem: List<MenuItem>,
        val isPaid: Boolean = false
    ) {
        data class MenuItem(
            val name: String,
            val price: Double
        )
    }

    data class InvoiceDetail(
        val items: List<Item>,
        val fees: List<Fee>
    ) {
        data class Item(
            val name: String,
            val price: Double,
            val qty: Int,
            val total: Double,
            val member: List<String>
        )

        data class Fee(
            val name: String,
            val price: Double
        )
    }
}