package sdumchykov.androidApp.presentation.viewPager.addContacts.adapter.viewHolder

import androidx.recyclerview.widget.RecyclerView
import sdumchykov.androidApp.databinding.AddContactsItemBinding
import sdumchykov.androidApp.domain.model.User
import sdumchykov.androidApp.presentation.utils.ext.gone
import sdumchykov.androidApp.presentation.utils.ext.setImageCacheless
import sdumchykov.androidApp.presentation.utils.ext.visible
import sdumchykov.androidApp.presentation.viewPager.addContacts.adapter.listener.UsersListener

class UsersViewHolder(
    private val binding: AddContactsItemBinding,
    private val usersListener: UsersListener
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var contact: User

    fun bindTo(user: User) {
        contact = user

        contact.apply {
            with(binding) {
                textViewMainName.text = user.name
                textViewMainProfession.text = user.career
                imageViewPhoto.setImageCacheless(user.image)

            }
            setListeners(user)
        }
    }

    private fun setListeners(user: User) {
        with(binding) {
            root.setOnClickListener {
                turnStateToAdded()
                usersListener.onUserClickAction(user, adapterPosition)
            }
        }
    }

    private fun turnStateToAdded() {
        with(binding) {
            textViewAddContactContactsAdd.gone()
            imageAddContactsButtonPlus.gone()
            imageAddContactsCheck.visible()
        }
    }
}
