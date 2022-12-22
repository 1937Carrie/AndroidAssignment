package sdumchykov.task2

import androidx.annotation.DrawableRes

data class Contact(
    val name: String,
    val profession: String,
    @DrawableRes
    val photo: Int?
) {
    @DrawableRes
    val deleteIcon: Int = R.drawable.ic_delete
}