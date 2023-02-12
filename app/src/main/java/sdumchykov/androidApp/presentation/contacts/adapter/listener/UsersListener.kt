package sdumchykov.androidApp.presentation.contacts.adapter.listener

import sdumchykov.androidApp.domain.model.UserModel

interface UsersListener {
    fun onUserClickAction(userModel: UserModel, adapterPosition: Int)
    fun onTrashIconClickAction(userModel: UserModel, userIndex: Int)
    fun onContactRemove(userModel: UserModel)
    fun onContactSelected(contact: UserModel)
    fun onMultiselectActivated()
    fun onContactSelectedStateChanged()
}