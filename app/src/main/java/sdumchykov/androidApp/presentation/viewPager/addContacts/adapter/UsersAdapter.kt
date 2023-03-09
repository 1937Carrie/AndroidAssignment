package sdumchykov.androidApp.presentation.viewPager.addContacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import sdumchykov.androidApp.databinding.ItemAddContactsBinding
import sdumchykov.androidApp.domain.model.User
import sdumchykov.androidApp.presentation.viewPager.addContacts.adapter.diffCallback.UsersDiffCallback
import sdumchykov.androidApp.presentation.viewPager.addContacts.adapter.listener.UsersListener
import sdumchykov.androidApp.presentation.viewPager.addContacts.adapter.viewHolder.UsersViewHolder

class UsersAdapter(
    val usersListener: UsersListener,
) : ListAdapter<User, UsersViewHolder>(UsersDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(
            ItemAddContactsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            usersListener
        )
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

}
