package id.djaka.splitbillapp.service.trip.repository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import id.djaka.splitbillapp.service.trip.TripModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
class TripFirebaseRepository : TripRepositoryContract {
    private val user = Firebase.auth.authStateChanged

    override val tripData: Flow<Map<String, TripModel>> by lazy {
        user.filterNotNull().flatMapConcat {
            Firebase.firestore.collection("trip").where {
                "ownerServerId" equalTo it.uid
            }.snapshots.map {
                it.documents.map {
                    it.data(TripModel.serializer())
                }.associateBy { it.id }
            }
        }
    }

    override suspend fun saveTripData(tripModel: TripModel) {
        Firebase.firestore.collection("trip").document(tripModel.id)
            .set(TripModel.serializer(), tripModel)
    }

    override suspend fun deleteTripData(tripId: String) {
        Firebase.firestore.collection("trip").document(tripId).delete()
    }


}