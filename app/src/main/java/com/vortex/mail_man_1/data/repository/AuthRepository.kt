import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.vortex.mail_man_1.utils.SessionManager
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser>
    suspend fun signOut()
    fun getCurrentUser(): FirebaseUser?
}

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val sessionManager: SessionManager
) : AuthRepository {
    override suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            Result.Success(result.user!!)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun signOut() {
        auth.signOut()
        sessionManager.clearSession()
    }

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser
} 