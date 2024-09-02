package com.dumchykov.socialnetworkdemo.ui.mycontacts.adapter.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.data.contactsprovider.Contact
import com.dumchykov.socialnetworkdemo.databinding.ItemContactBinding

class PlainViewHolder(
    private val binding: ItemContactBinding,
    private val onClick: (View, Contact) -> Unit,
    private val onDelete: (Contact) -> Unit,
    private val onChangeSelect: (Contact) -> Unit = {},
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(contact: Contact) {
        binding.root.setOnClickListener {
            onClick(binding.imageMain, contact)
        }
        binding.root.setOnLongClickListener {
            onChangeSelect(contact)
            return@setOnLongClickListener false
        }
        binding.textName.text = contact.name
        binding.textCareer.text = contact.career
        binding.imageDelete.setOnClickListener {
            onDelete(contact)
        }
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