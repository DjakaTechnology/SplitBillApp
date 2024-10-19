package id.djaka.splitbillapp.service.trip.repository

import id.djaka.splitbillapp.service.trip.TripModel
import kotlinx.coroutines.flow.Flow

interface TripRepositoryContract {
    val tripData: Flow<Map<String, TripModel>>

    suspend fun saveTripData(tripModel: TripModel)

    suspend fun deleteTripData(tripId: String)
}