package sdumchykov.androidApp.domain.model.requestModels

data class AuthorizeModel(
    val email: String,
    val password: String
)