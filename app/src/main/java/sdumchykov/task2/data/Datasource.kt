package sdumchykov.task2.data

import sdumchykov.task2.model.Contact

class Datasource {
    companion object {
        private var data = arrayListOf(
            Contact(
                name = "Ava Smith",
                profession = "Photograph",
                photo = "https://picsum.photos/200"
            ),
            Contact(
                name = "Ava Smith",
                profession = "Photograph",
                photo = "https://picsum.photos/200"
            ), Contact(
                name = "Ava Smith",
                profession = "Photograph",
                photo = "https://picsum.photos/200"
            ), Contact(
                name = "Ava Smith",
                profession = "Photograph",
                photo = "https://picsum.photos/200"
            ), Contact(
                name = "Ava Smith",
                profession = "Photograph",
                photo = "https://picsum.photos/200"
            ), Contact(
                name = "Ava Smith",
                profession = "Photograph",
                photo = "https://picsum.photos/200"
            ), Contact(
                name = "Ava Smith",
                profession = "Photograph",
                photo = "https://picsum.photos/200"
            ), Contact(
                name = "Ava Smith",
                profession = "Photograph",
                photo = "https://picsum.photos/200"
            ), Contact(
                name = "Ava Smith",
                profession = "Photograph",
                photo = "https://picsum.photos/200"
            )
        )

        fun get() = data

        fun set(value: ArrayList<Contact>) {
            data = value
        }
    }

}