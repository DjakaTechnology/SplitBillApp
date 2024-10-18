package id.djaka.splitbillapp.trip

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import cafe.adriel.voyager.core.model.ScreenModel
import id.djaka.splitbillapp.service.bill.BillRepository
import id.djaka.splitbillapp.service.trip.TripRepository
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class TripScreenModel(
    val billRepository: BillRepository,
    val tripRepository: TripRepository
) : ScreenModel {
    var tripId: String = ""
    var selectedMember: String? by mutableStateOf(null)

    val billSummary by lazy {
        billRepository.billsData.map {
            it.filter { it.value.tripId == tripId }
        }.buffer(capacity = 1)
    }

    val trip by lazy {
        tripRepository.tripData.map {
            it[tripId]
        }
    }

    val tripSummary by lazy {
        combine(
            snapshotFlow { selectedMember },
            billSummary,
        ) { member, bill ->
            if (member == null) {
                return@combine bill
            } else {
                bill.filter { it.value.members.any { it.id == member } }
            }
        }.map { it.values.toList() }
    }

    val memberBillSummary by lazy {
        billSummary.map {
            val members = mutableMapOf<String, MemberSummary>()

            it.forEach { (_, bill) ->
                bill.members.forEach { member ->
                    val currentData = members.getOrPut(member.id) {
                        MemberSummary(member.id, member.name, 0.0)
                    }

                    var total = currentData.total
                    if (!member.isPaid) {
                        bill.items.forEach { item ->
                            if (item.memberIds.contains(member.id)) {
                                total += item.pricePerMember
                            }
                        }
                        bill.feeItems.forEach { item ->
                            total += item.price / bill.members.size
                        }
                    }

                    members[member.id] = currentData.copy(
                        total = total
                    )

                }
            }

            members.toMap()
        }.map { it.values.toList() }
    }

    data class MemberSummary(
        val id: String,
        val name: String,
        val total: Double,
    )
}