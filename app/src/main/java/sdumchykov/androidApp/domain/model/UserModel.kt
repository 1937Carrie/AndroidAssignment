package sdumchykov.androidApp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: Int,
    val name: String,
    val profession: String,
    val photo: String
) : Parcelable
