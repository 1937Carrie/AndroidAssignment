package com.dumchykov.socialnetworkdemo.ui.addcontacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dumchykov.socialnetworkdemo.data.contactsprovider.Contact
import com.dumchykov.socialnetworkdemo.databinding.ItemContactAddBinding
import com.dumchykov.socialnetworkdemo.ui.mycontacts.adapter.ContactsAdapter

class AddContactsAdapter(
    private val onClickListener: (Int, String) -> Unit,
    private val onAddListener: (Int) -> Unit,
) : ListAdapter<Contact, RecyclerView.ViewHolder>(ContactsAdapter.Diff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AddContactsViewHolder(
            ItemContactAddBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as AddContactsViewHolder
        holder.onBind(getItem(position), onClickListener, onAddListener)
    }
}