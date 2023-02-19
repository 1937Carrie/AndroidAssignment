package sdumchykov.androidApp.data.repository

import android.annotation.SuppressLint
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

    @SuppressLint("Range")
    override suspend fun getRealUsers() {
        val phones = context.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
        )

        if (phones != null) {
            val userModels = ArrayList<UserModel>()
            while (phones.moveToNext()) {
                val name =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val contact =
                    UserModel(userModels.size, name, phoneNumber, Constants.HARDCODED_IMAGE_URL)

                userModels.add(contact)
            }
//            parentViewModel.addData(userModels)
            phones.close()
        }
    }
}
