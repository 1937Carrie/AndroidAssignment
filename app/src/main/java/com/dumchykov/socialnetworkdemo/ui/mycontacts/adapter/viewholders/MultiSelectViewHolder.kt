package com.dumchykov.socialnetworkdemo.ui.mycontacts.adapter.viewholders

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.data.contactsprovider.Contact
import com.dumchykov.socialnetworkdemo.databinding.ItemContactMultiselectBinding
import com.dumchykov.socialnetworkdemo.ui.util.setImageWithGlide

class MultiSelectViewHolder(
    private val binding: ItemContactMultiselectBinding,
    private val onChangeSelect: (Contact) -> Unit = {},
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(context: Context, contact: Contact) {
        binding.root.setOnClickListener {
            onChangeSelect(contact)
        }
        binding.imageSelector.setImageDrawable(
            AppCompatResources.getDrawable(
                context,
                if (contact.isSelected) R.drawable.ic_selector_selected else R.drawable.ic_selector
            )
        )
        binding.textName.text = contact.name
        binding.textCareer.text = contact.career
        binding.imageMain.setImageWithGlide("https://www.reuters.com/resizer/v2/MKQZUV67IFKAHDUNK4LJATIVMQ.jpg?auth=85a0616067eb4e93c8895d334072973babbfedb1376eb30339e6988218abc7ab")
        setTransitionName(contact)
    }

    private fun setTransitionName(contact: Contact) {
        binding.imageMain.transitionName = "${contact.id}_${contact.name}"
    }
}