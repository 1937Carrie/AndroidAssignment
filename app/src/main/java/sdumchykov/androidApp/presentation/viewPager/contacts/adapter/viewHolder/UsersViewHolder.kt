package sdumchykov.androidApp.presentation.viewPager.contacts.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sdumchykov.androidApp.R
import sdumchykov.androidApp.databinding.ContactItemBinding
import sdumchykov.androidApp.domain.model.UserModel
import sdumchykov.androidApp.domain.utils.Constants.HARDCODED_IMAGE_URL
import sdumchykov.androidApp.presentation.utils.ext.setImageCacheless
import sdumchykov.androidApp.presentation.viewPager.contacts.adapter.listener.UsersListener

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

    private fun setListeners(user: UserModel) {
        with(binding) {
            imageButtonDelete.setOnClickListener {
                usersListener.onTrashIconClickAction(user, adapterPosition)
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
