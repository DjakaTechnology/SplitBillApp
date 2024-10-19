package id.djaka.splitbillapp.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import id.djaka.splitbillapp.service.bill.BillRepository
import id.djaka.splitbillapp.service.trip.TripModel
import id.djaka.splitbillapp.service.trip.TripRepository
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class HomeScreenModel(
    billRepository: BillRepository,
    private val tripRepository: TripRepository,
) : ScreenModel {
    val bills = billRepository.billsData.map {
        it.values.toList()
    }

    val trip = tripRepository.tripData.buffer(1)

    val tripList = trip.map {
        it.values.toList().sortedByDescending { it.endDate }
    }.catch {
        println("Error: $it")
    }

    fun onCreate() {
        screenModelScope.launch {
            val data = tripRepository.tripData.first() ?: return@launch
            if (data.isEmpty()) {
                tripRepository.saveTripData(
                    TripModel(
                        id = Uuid.random().toHexString(),
                        name = "No Trip",
                        ownerServerId = Firebase.auth.currentUser?.uid
                    )
                )
            }
        }
    }

    fun addTrip(it: HomeAddTripSheetState) {
        screenModelScope.launch {
            tripRepository.saveTripData(
                TripModel(
                    id = Uuid.random().toHexString(),
                    name = it.name,
                    startDate = it.startDate,
                    endDate = it.endDate,
                    ownerServerId = Firebase.auth.currentUser?.uid
                )
            )
        }
    }
}