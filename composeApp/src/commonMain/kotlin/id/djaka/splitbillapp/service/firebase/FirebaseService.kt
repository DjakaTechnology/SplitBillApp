package id.djaka.splitbillapp.service.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.flow

class FirebaseService {
    val userFlow by lazy {
        flow {
            val userFlow = Firebase.auth.currentUser
            emit(userFlow)
            Firebase.auth.authStateChanged.collect {
                if (userFlow != it) {
                    emit(userFlow)
                }
            }
        }
    }

    val user
        get() = Firebase.auth.currentUser

    val isLogged
        get() = Firebase.auth.currentUser != null
}