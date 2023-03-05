package sdumchykov.androidApp.presentation.viewPager.contacts.adapter.listener

import sdumchykov.androidApp.domain.model.User

interface UsersListener {
    fun onUserClickAction(user: User, adapterPosition: Int)
    fun onContactRemove(user: User)
    fun onMultiselectActivated()
    fun onContactSelectedStateChanged()
}
