package com.dumchykov.socialnetworkdemo.ui.mycontacts.adapter.viewholders

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.data.contactsprovider.Contact
import com.dumchykov.socialnetworkdemo.databinding.ItemContactMultiselectBinding

class MultiSelectViewHolder(
    private val binding: ItemContactMultiselectBinding,
    private val onChangeSelect: (Contact) -> Unit = {},
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(context: Context, contact: Contact) {
        binding.root.setOnClickListener {
            onChangeSelect(contact)
        }
//        Glide
//            .with(binding.imageSelector)
//            .load(if (contact.isSelected) R.drawable.ic_selector_selected else R.drawable.ic_selector)
//            .into(binding.imageSelector)
        binding.imageSelector.setImageDrawable(
            AppCompatResources.getDrawable(
                context,
                if (contact.isSelected) R.drawable.ic_selector_selected else R.drawable.ic_selector
            )
        )
        binding.textName.text = contact.name
        binding.textCareer.text = contact.career
        Glide
            .with(binding.imageMain)
            .load("https://www.reuters.com/resizer/v2/MKQZUV67IFKAHDUNK4LJATIVMQ.jpg?auth=85a0616067eb4e93c8895d334072973babbfedb1376eb30339e6988218abc7ab")
            .circleCrop()
            .placeholder(R.drawable.image_main)
            .into(binding.imageMain)
        setTransitionName(contact)
    }

    private fun setTransitionName(contact: Contact) {
        binding.imageMain.transitionName = "${contact.id}_${contact.name}"
    }
}