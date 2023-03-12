package sdumchykov.androidApp.data.db

import sdumchykov.androidApp.domain.model.User
import sdumchykov.androidApp.domain.utils.Constants.HARDCODED_IMAGE_URL
import javax.inject.Inject

class InMemoryDb @Inject constructor() {
    fun getHardcodedUsers(): List<User> = listOf(
        User(
            id = 0,
            name = "Function Let",
            career = "Photograph",
            image = HARDCODED_IMAGE_URL
        ),
        User(
            id = 1,
            name = "Function With",
            career = "Photograph",
            image = HARDCODED_IMAGE_URL
        ),
        User(
            id = 2,
            name = "Function Run",
            career = "Photograph",
            image = HARDCODED_IMAGE_URL
        ),
        User(
            id = 3,
            name = "Function Apply",
            career = "Photograph",
            image = HARDCODED_IMAGE_URL
        ),
        User(
            id = 4,
            name = "Function Also",
            career = "Photograph",
            image = HARDCODED_IMAGE_URL
        ),
        User(
            id = 5,
            name = "Readonly List",
            career = "Photograph",
            image = HARDCODED_IMAGE_URL
        ),
        User(
            id = 6,
            name = "Readonly Map",
            career = "Photograph",
            image = HARDCODED_IMAGE_URL
        ),
        User(
            id = 7,
            name = "Lazy Property",
            career = "Photograph",
            image = HARDCODED_IMAGE_URL
        ),
        User(
            id = 8,
            name = "If Expression",
            career = "Photograph",
            image = HARDCODED_IMAGE_URL
        ),
        User(
            id = 9,
            name = "Nullable Boolean",
            career = "Photograph",
            image = HARDCODED_IMAGE_URL
        ),
        User(
            id = 1000,
            name = "Nullable Boolean",
            career = "Photograph",
            image = HARDCODED_IMAGE_URL
        )
    )
}
