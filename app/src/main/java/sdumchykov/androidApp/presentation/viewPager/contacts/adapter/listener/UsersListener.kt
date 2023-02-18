package sdumchykov.androidApp.presentation.viewPager.contacts.adapter.listener

import sdumchykov.androidApp.domain.model.UserModel

interface UsersListener {
    fun onUserClickAction(userModel: UserModel, adapterPosition: Int)
    fun onTrashIconClickAction(userModel: UserModel, userIndex: Int)
    fun onContactRemove(userModel: UserModel)
    fun onMultiselectActivated()
    fun onContactSelectedStateChanged()
}
