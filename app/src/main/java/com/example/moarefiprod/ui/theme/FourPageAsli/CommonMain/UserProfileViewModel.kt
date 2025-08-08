
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserProfileViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // فیلدهای قابل تغییر با ستتر
    var firstName = mutableStateOf("")
        private set
    fun setFirstName(value: String) { firstName.value = value }

    var lastName = mutableStateOf("")
        private set
    fun setLastName(value: String) { lastName.value = value }

    var birthDay = mutableStateOf("")
        private set
    fun setBirthDay(value: String) { birthDay.value = value }

    var birthMonth = mutableStateOf("")
        private set
    fun setBirthMonth(value: String) { birthMonth.value = value }

    var birthYear = mutableStateOf("")
        private set
    fun setBirthYear(value: String) { birthYear.value = value }

    var gender = mutableStateOf("")
        private set
    fun setGender(value: String) {
        gender.value = value
        updateProfileImageBasedOnGender(value) // ⬅ بروزرسانی عکس بلافاصله بعد از تغییر جنسیت
    }

    var email = mutableStateOf("")
        private set

    var profileImage = mutableStateOf<String?>(null)
        private set

    init {
        loadUserData()
    }

    fun loadUserData() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) return@addOnSuccessListener

                setFirstName(doc.getString("firstName") ?: "")
                setLastName(doc.getString("lastName") ?: "")

                val birthday = doc.getString("birthday") ?: ""
                val parts = birthday.split("-")
                if (parts.size == 3) {
                    setBirthYear(parts[0])
                    setBirthMonth(parts[1])
                    setBirthDay(parts[2])
                }

                setGender(doc.getString("gender") ?: "")
                profileImage.value = doc.getString("profileImage")
                email.value = doc.getString("email") ?: ""
            }
    }

    fun saveProfile(onSuccess: () -> Unit, onFailure: () -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val userData = hashMapOf(
            "firstName" to firstName.value,
            "lastName" to lastName.value,
            "birthday" to "${birthYear.value}-${birthMonth.value}-${birthDay.value}",
            "gender" to gender.value,
            "profileImage" to (profileImage.value ?: "default"),
            "email" to FirebaseAuth.getInstance().currentUser?.email,
            "uid" to uid,
            "enrolledCourses" to listOf<String>()
        )

        db.collection("users").document(uid)
            .set(userData)
            .addOnSuccessListener {
                loadUserData()
                onSuccess()
            }
            .addOnFailureListener { onFailure() }
    }

    fun updateProfileImageBasedOnGender(gender: String) {
        profileImage.value = when (gender) {
            "مرد" -> "profm"
            "زن" -> "profw"
            else -> null
        }
    }

}
