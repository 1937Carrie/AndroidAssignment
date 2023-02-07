package sdumchykov.androidApp.data.db

import sdumchykov.androidApp.domain.model.UserModel
import sdumchykov.androidApp.domain.utils.Constants.HARDCODED_IMAGE_URL
import javax.inject.Inject

class InMemoryDb @Inject constructor() {
    fun getHardcodedUsers(): List<UserModel> = listOf(
        UserModel(
            id = 0,
            name = "Function Let",
            profession = "Photograph",
            photo = HARDCODED_IMAGE_URL
        ),
        UserModel(
            id = 1,
            name = "Function With",
            profession = "Photograph",
            photo = HARDCODED_IMAGE_URL
        ),
        UserModel(
            id = 2,
            name = "Function Run",
            profession = "Photograph",
            photo = HARDCODED_IMAGE_URL
        ),
        UserModel(
            id = 3,
            name = "Function Apply",
            profession = "Photograph",
            photo = HARDCODED_IMAGE_URL
        ),
        UserModel(
            id = 4,
            name = "Function Also",
            profession = "Photograph",
            photo = HARDCODED_IMAGE_URL
        ),
        UserModel(
            id = 5,
            name = "Readonly List",
            profession = "Photograph",
            photo = HARDCODED_IMAGE_URL
        ),
        UserModel(
            id = 6,
            name = "Readonly Map",
            profession = "Photograph",
            photo = HARDCODED_IMAGE_URL
        ),
        UserModel(
            id = 7,
            name = "Lazy Property",
            profession = "Photograph",
            photo = HARDCODED_IMAGE_URL
        ),
        UserModel(
            id = 8,
            name = "If Expression",
            profession = "Photograph",
            photo = HARDCODED_IMAGE_URL
        ),
        UserModel(
            id = 9,
            name = "Nullable Boolean",
            profession = "Photograph",
            photo = HARDCODED_IMAGE_URL
        ),
        UserModel(
            id = 1000,
            name = "Nullable Boolean",
            profession = "Photograph",
            photo = HARDCODED_IMAGE_URL
        )
    )
}