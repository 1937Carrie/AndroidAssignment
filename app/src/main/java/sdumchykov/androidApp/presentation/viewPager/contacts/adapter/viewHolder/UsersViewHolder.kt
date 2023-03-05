package sdumchykov.androidApp.presentation.viewPager.contacts.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sdumchykov.androidApp.R
import sdumchykov.androidApp.databinding.ContactItemBinding
import sdumchykov.androidApp.domain.model.User
import sdumchykov.androidApp.presentation.utils.ext.setImageCacheless
import sdumchykov.androidApp.presentation.viewPager.contacts.adapter.listener.UsersListener

class UsersViewHolder(
    private val binding: ContactItemBinding,
    private val usersListener: UsersListener,
    private val multiSelect: Boolean,
    private val selectedItems: ArrayList<User>
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var contact: User

    fun bindTo(user: User) {
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
                textViewMainProfession.text = user.career
                imageViewPhoto.setImageCacheless(user.image)

                if (binding.checkBoxSelectedState.visibility == View.VISIBLE) {
                    val marginLayoutParams =
                        imageViewPhoto.layoutParams as ViewGroup.MarginLayoutParams
                    val imageViewPhotoResources = imageViewPhoto.context.resources
                    val margin =
                        (
                                imageViewPhotoResources.getDimension(R.dimen.dimension_8dp) /
                                        imageViewPhotoResources.displayMetrics.density
                                ).toInt()
                    marginLayoutParams.setMargins(margin, margin, margin, margin)
                    imageViewPhoto.layoutParams = marginLayoutParams
                }
            }
            setListeners(user)
        }
    }

    private fun setListeners(user: User) {
        with(binding) {
            imageButtonDelete.setOnClickListener {
                usersListener.onContactRemove(user)
            }

            root.setOnClickListener {
                if (!multiSelect) {
                    usersListener.onUserClickAction(user, adapterPosition)
                } else {
                    checkBoxSelectedState.isChecked = !checkBoxSelectedState.isChecked
                    selectItem(contact)
                    usersListener.onContactSelectedStateChanged()
                }
            }

            checkBoxSelectedState.setOnClickListener {
                selectItem(contact)
                usersListener.onContactSelectedStateChanged()
            }

            root.setOnLongClickListener {
                if (!multiSelect) {
                    usersListener.onMultiselectActivated()
                    selectItem(contact)
                }
                return@setOnLongClickListener true
            }
        }
    }

    // helper function that adds/removes an item to the list depending on the app's state
    private fun selectItem(user: User) {
        if (selectedItems.contains(user)) {
            selectedItems.remove(user)
            binding.checkBoxSelectedState.isChecked = false
        } else {
            selectedItems.add(user)
            binding.checkBoxSelectedState.isChecked = true
        }
    }
}
