package sdumchykov.androidApp.presentation.viewPager.contacts.adapter.diffCallback

import androidx.recyclerview.widget.DiffUtil
import sdumchykov.androidApp.domain.model.User

class UsersDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem == newItem
}
