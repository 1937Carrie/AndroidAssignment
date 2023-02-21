package sdumchykov.androidApp.data.repository

import android.content.Context
import android.provider.ContactsContract
import dagger.hilt.android.qualifiers.ApplicationContext
import sdumchykov.androidApp.data.db.InMemoryDb
import sdumchykov.androidApp.domain.model.UserModel
import sdumchykov.androidApp.domain.repository.UsersRepository
import sdumchykov.androidApp.domain.utils.Constants
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val inMemoryDb: InMemoryDb,
) : UsersRepository {
    override suspend fun getHardcodedUsers() = inMemoryDb.getHardcodedUsers()

    override suspend fun getRealUsers(): List<UserModel> {
        val phones = context.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
        )
        val realUsersList = ArrayList<UserModel>()

        if (phones != null) {
            while (phones.moveToNext()) {
                val nameIndex =
                    phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val name = if (nameIndex != -1) phones.getString(nameIndex) else "Stub"

                val phoneIndex =
                    phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val phoneNumber = if (phoneIndex != -1) phones.getString(phoneIndex) else "Stub"

                val contact =
                    UserModel(realUsersList.size, name, phoneNumber, Constants.HARDCODED_IMAGE_URL)

                realUsersList.add(contact)
            }
            phones.close()
        }

        return realUsersList
    }
}
