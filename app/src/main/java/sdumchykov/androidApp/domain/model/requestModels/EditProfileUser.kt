package sdumchykov.androidApp.domain.model.requestModels

data class EditProfileUser(
    val name: String? = "",
    val phone: String? = "",
    val address: String? = "",
    val career: String? = "",
    val birthday: String? = "",
    val facebook: String? = "",
    val instagram: String? = "",
    val twitter: String? = "",
    val linkedin: String? = "",
    val image: String? = ""
)
