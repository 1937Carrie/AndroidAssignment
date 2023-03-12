package sdumchykov.androidApp.data.repository

import android.content.Context
import android.provider.ContactsContract
import dagger.hilt.android.qualifiers.ApplicationContext
import sdumchykov.androidApp.data.db.InMemoryDb
import sdumchykov.androidApp.domain.model.User
import sdumchykov.androidApp.domain.repository.LocalUsersRepository
import javax.inject.Inject

class LocalLocalUsersRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val inMemoryDb: InMemoryDb
) : LocalUsersRepository {
    override suspend fun getHardcodedUsers() = inMemoryDb.getHardcodedUsers()

    override suspend fun getRealUsers(): List<User> {
        val phones = context.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
        )
        val realUsersList = ArrayList<User>()

        if (phones != null) {
            while (phones.moveToNext()) {
                val nameIndex =
                    phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val name = if (nameIndex != -1) phones.getString(nameIndex) else "Stub"

                val phoneIndex =
                    phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val phoneNumber = if (phoneIndex != -1) phones.getString(phoneIndex) else "Stub"

                val contactImageIndex =
                    phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
//                Log.i("imageIndex", contactImageIndex.toString())
                val contactImage =
                    if (contactImageIndex != -1) phones.getString(contactImageIndex) else "Stub"
//                Log.i("imagePath", contactImage ?: "null")

                val contact = User(
                    id = realUsersList.size,
                    name = name,
                    phone = phoneNumber,
                    image = contactImage
                )

                realUsersList.add(contact)
            }
            phones.close()
        }

        return realUsersList
    }

}
