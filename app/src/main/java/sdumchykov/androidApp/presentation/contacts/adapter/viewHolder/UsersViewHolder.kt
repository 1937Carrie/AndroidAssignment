package sdumchykov.androidApp.presentation.contacts.adapter.viewHolder

import androidx.recyclerview.widget.RecyclerView
import sdumchykov.androidApp.databinding.ContactItemBinding
import sdumchykov.androidApp.domain.model.UserModel
import sdumchykov.androidApp.domain.utils.Constants.HARDCODED_IMAGE_URL
import sdumchykov.androidApp.presentation.contacts.adapter.listener.UsersListener
import sdumchykov.androidApp.presentation.utils.ext.setImageCacheless

class UsersViewHolder(
    private val binding: ContactItemBinding,
    private val usersListener: UsersListener,

    ) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(user: UserModel) {
        with(binding) {
            textViewMainName.text = user.name
            textViewMainProfession.text = user.profession
            imageViewPhoto.setImageCacheless(HARDCODED_IMAGE_URL)

            setListeners(user)
        }
    }

    private fun setListeners(user: UserModel) {
        binding.root.setOnClickListener {
            usersListener.onUserClickAction(user)
        }
        binding.imageButtonDelete.setOnClickListener {
            usersListener.onTrashIconClickAction(user, adapterPosition)
        }
    }
}