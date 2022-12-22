package sdumchykov.task2

import androidx.annotation.DrawableRes

data class Contact(
    val name: String,
    val profession: String,
    @DrawableRes
    val photo: Int?
) {
    @DrawableRes
    val trashIcon: Int = R.drawable.ic_delete
}