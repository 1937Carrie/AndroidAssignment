package com.dumchykov.socialnetworkdemo.ui.mycontacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.data.contactsprovider.Contact
import com.dumchykov.socialnetworkdemo.databinding.ItemContactBinding

class ContactsAdapter(
    private val onDelete: (Contact) -> Unit = {},
) : ListAdapter<Contact, ContactsAdapter.ViewHolder>(Diff()) {
    inner class ViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(contact: Contact) {
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class Diff() : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }
}