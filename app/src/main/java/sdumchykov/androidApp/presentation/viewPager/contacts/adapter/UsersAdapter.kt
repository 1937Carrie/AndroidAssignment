package sdumchykov.androidApp.presentation.viewPager.contacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import sdumchykov.androidApp.databinding.ContactItemBinding
import sdumchykov.androidApp.domain.model.UserModel
import sdumchykov.androidApp.presentation.viewPager.contacts.MyContactsViewModel
import sdumchykov.androidApp.presentation.viewPager.contacts.adapter.diffCallback.UsersDiffCallback
import sdumchykov.androidApp.presentation.viewPager.contacts.adapter.listener.UsersListener
import sdumchykov.androidApp.presentation.viewPager.contacts.adapter.viewHolder.UsersViewHolder

class UsersAdapter(
    private val usersListener: UsersListener,
    private var multiSelect: Boolean = false
) : ListAdapter<UserModel, UsersViewHolder>(UsersDiffCallback()) {

    private val selectedItems = arrayListOf<UserModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(
            ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            usersListener,
            multiSelect,
            selectedItems
        )
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    fun enableMultiselect() {
        multiSelect = true
    }

    fun areAllItemsUnselected(): Boolean {
        if (selectedItems.isEmpty()) {
            multiSelect = false
            return true
        }
        return false
    }

    fun removeSelectedItems(myContactsViewModel: MyContactsViewModel) {
        for (item in selectedItems) {
            usersListener.onContactRemove(item)
        }
        selectedItems.clear()
        if (myContactsViewModel.userLiveData.value?.isEmpty() == true) {
            multiSelect = false
        }
    }

    fun unselectItems() {
        selectedItems.clear()
        multiSelect = false
    }
}
