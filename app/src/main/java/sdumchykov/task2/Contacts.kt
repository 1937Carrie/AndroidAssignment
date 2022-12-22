package sdumchykov.task2

import android.content.res.Resources

fun contactList(resources: Resources): ArrayList<Contact> {
    return arrayListOf(
        Contact(name = "Ava Smith", profession = "Photograph", photo = R.drawable.tmp_rounded_image)
    )
}