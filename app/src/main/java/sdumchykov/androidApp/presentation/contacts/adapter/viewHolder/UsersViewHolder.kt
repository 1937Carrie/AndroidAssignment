package sdumchykov.androidApp.presentation.contacts.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sdumchykov.androidApp.R
import sdumchykov.androidApp.databinding.ContactItemBinding
import sdumchykov.androidApp.domain.model.UserModel
import sdumchykov.androidApp.domain.utils.Constants.HARDCODED_IMAGE_URL
import sdumchykov.androidApp.presentation.contacts.adapter.listener.UsersListener
import sdumchykov.androidApp.presentation.utils.ext.setImageCacheless


class UsersViewHolder(
    private val binding: ContactItemBinding,
    private val usersListener: UsersListener,
    private val multiSelect: Boolean,
    private val selectedItems: ArrayList<UserModel>

) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var contact: UserModel

    fun bindTo(user: UserModel) {
        contact = user

        contact.apply {
            with(binding) {
                if (multiSelect) {
                    binding.checkBoxSelectedState.visibility = View.VISIBLE
                    binding.imageButtonDelete.visibility = View.INVISIBLE
                } else {
                    binding.checkBoxSelectedState.visibility = View.GONE
                    binding.imageButtonDelete.visibility = View.VISIBLE
                }

                if (selectedItems.contains(contact)) {
                    binding.checkBoxSelectedState.isChecked = true
                }

                textViewMainName.text = user.name
                textViewMainProfession.text = user.profession
                imageViewPhoto.setImageCacheless(HARDCODED_IMAGE_URL)
//                imageViewPhoto.setBackgroundResource(R.mipmap.ic_launcher_custom)
                if (binding.checkBoxSelectedState.visibility == View.VISIBLE) {
                    val lp = imageViewPhoto.layoutParams as ViewGroup.MarginLayoutParams
                    val margin =
                        (imageViewPhoto.context.resources.getDimension(R.dimen.dimension_8dp) / imageViewPhoto.context.resources.displayMetrics.density).toInt()
                    lp.setMargins(margin, margin, margin, margin)
                    imageViewPhoto.layoutParams = lp

                }
            }
            setListeners(user)
        }
    }

    private fun setListeners(user: UserModel) {
        binding.imageButtonDelete.setOnClickListener {
            usersListener.onTrashIconClickAction(user, adapterPosition)
        }

        binding.root.setOnClickListener {
            if (!multiSelect) {
//                usersListener.onContactSelected(contact) //TODO what is it?
                usersListener.onUserClickAction(user, adapterPosition)
            } else {
                binding.checkBoxSelectedState.isChecked = !binding.checkBoxSelectedState.isChecked
                selectItem(contact)
                usersListener.onContactSelectedStateChanged()
            }
        }

        binding.checkBoxSelectedState.setOnClickListener {
            selectItem(contact)
            usersListener.onContactSelectedStateChanged()
        }

        binding.root.setOnLongClickListener {
            if (!multiSelect) {
                usersListener.onMultiselectActivated()
                selectItem(contact)
            }
            return@setOnLongClickListener true
        }
    }

    // helper function that adds/removes an item to the list depending on the app's state
    private fun selectItem(userModel: UserModel) {
        if (selectedItems.contains(userModel)) {
            selectedItems.remove(userModel)
            binding.checkBoxSelectedState.isChecked = false
        } else {
            selectedItems.add(userModel)
            binding.checkBoxSelectedState.isChecked = true
        }
    }
}