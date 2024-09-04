package com.dumchykov.socialnetworkdemo.ui.screens.addcontacts.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dumchykov.socialnetworkdemo.data.contactsprovider.Contact
import com.dumchykov.socialnetworkdemo.databinding.ItemContactAddBinding
import com.dumchykov.socialnetworkdemo.ui.util.setImageWithGlide

class AddContactsViewHolder(
    private val binding: ItemContactAddBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(
        contact: Contact,
        onClickListener: (Int, String) -> Unit,
        onAddListener: (Int) -> Unit,
    ) {
        binding.imageMain.setImageWithGlide("https://www.reuters.com/resizer/v2/MKQZUV67IFKAHDUNK4LJATIVMQ.jpg?auth=85a0616067eb4e93c8895d334072973babbfedb1376eb30339e6988218abc7ab")
        binding.textName.text = contact.name
        binding.textCareer.text = contact.career

        binding.root.setOnClickListener {
            onClickListener(contact.id, contact.name)
        }

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