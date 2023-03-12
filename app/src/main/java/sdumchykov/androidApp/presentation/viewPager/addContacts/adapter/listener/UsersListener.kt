package sdumchykov.androidApp.presentation.viewPager.addContacts.adapter.listener

import sdumchykov.androidApp.domain.model.User

interface UsersListener {
    fun onUserClickAction(user: User, adapterPosition: Int)
}
