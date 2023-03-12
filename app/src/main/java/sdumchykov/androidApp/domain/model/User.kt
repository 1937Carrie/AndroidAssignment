package sdumchykov.androidApp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val address: String? = "",
    val birthday: String? = "",
    val career: String? = "",
    val created_at: String? = "",
    val email: String? = "",
    val facebook: String? = "",
    val id: Int = 0,
    val image: String? = "",
    val instagram: String? = "",
    val linkedin: String? = "",
    val name: String? = "",
    val phone: String? = "",
    val twitter: String? = "",
    val updated_at: String? = ""
) : Parcelable