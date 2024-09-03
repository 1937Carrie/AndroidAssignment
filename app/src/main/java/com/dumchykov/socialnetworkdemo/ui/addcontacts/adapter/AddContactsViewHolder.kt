package com.dumchykov.socialnetworkdemo.ui.addcontacts.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dumchykov.socialnetworkdemo.data.contactsprovider.Contact
import com.dumchykov.socialnetworkdemo.databinding.ItemContactAddBinding

class AddContactsViewHolder(
    private val binding: ItemContactAddBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(
        contact: Contact,
        onAddListener: (Int) -> Unit,
    ) {
        binding.textName.text = contact.name
        binding.textCareer.text = contact.career

        if (contact.isAdded) {
            binding.layoutAddContact.visibility = View.GONE
            binding.imageAdded.visibility = View.VISIBLE
        } else {
            binding.layoutAddContact.visibility = View.VISIBLE
            binding.imageAdded.visibility = View.GONE

            binding.layoutAddContact.setOnClickListener {
                onAddListener(contact.id)
            }
        }
    }
}