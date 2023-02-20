package sdumchykov.androidApp.presentation.viewPager.myProfile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import sdumchykov.androidApp.domain.storage.Storage
import sdumchykov.androidApp.domain.utils.Constants
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val storage: Storage
) : ViewModel() {
    fun getFetchContactList(): Boolean {
        return storage.getBoolean(Constants.FETCH_CONTACT_LIST_KEY)
    }

    fun setFetchContactList(state: Boolean) {
        storage.save(Constants.FETCH_CONTACT_LIST_KEY, state)
    }
}
