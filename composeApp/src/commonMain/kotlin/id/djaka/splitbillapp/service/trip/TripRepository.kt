package id.djaka.splitbillapp.service.trip

import id.djaka.splitbillapp.service.firebase.FirebaseService
import id.djaka.splitbillapp.service.trip.repository.TripFirebaseRepository
import id.djaka.splitbillapp.service.trip.repository.TripRepositoryContract
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat

class TripRepository(
    private val firebaseRepository: TripFirebaseRepository,
    private val localRepository: TripFirebaseRepository,
    private val firebaseService: FirebaseService
) : TripRepositoryContract {

    private val repo
        get() = if (firebaseService.isLogged) firebaseRepository else localRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    override val tripData: Flow<Map<String, TripModel>> by lazy {
        firebaseService.userFlow.flatMapConcat { repo.tripData }
    }

    override suspend fun saveTripData(tripModel: TripModel) {
        repo.saveTripData(tripModel)
    }

    override suspend fun deleteTripData(tripId: String) {
        repo.deleteTripData(tripId)
    }
}