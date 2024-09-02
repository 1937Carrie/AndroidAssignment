package com.dumchykov.socialnetworkdemo.ui.mycontacts.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dumchykov.socialnetworkdemo.data.contactsprovider.Contact
import com.dumchykov.socialnetworkdemo.databinding.ItemContactBinding
import com.dumchykov.socialnetworkdemo.databinding.ItemContactMultiselectBinding
import com.dumchykov.socialnetworkdemo.ui.mycontacts.adapter.viewholders.MultiSelectViewHolder
import com.dumchykov.socialnetworkdemo.ui.mycontacts.adapter.viewholders.PlainViewHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ContactsAdapter(
    private val context: Context,
    private val onClick: (View, Contact) -> Unit = { _, _ -> },
    private val onDelete: (Contact) -> Unit = {},
    private val onChangeSelect: (Contact) -> Unit = {},
) : ListAdapter<Contact, RecyclerView.ViewHolder>(Diff()) {
    private val isMultiSelected = MutableStateFlow(false)

    override fun getItemViewType(position: Int): Int {
        return when (isMultiSelected.value) {
            true -> 0
            false -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                val binding =
                    ItemContactMultiselectBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                return MultiSelectViewHolder(
                    binding = binding,
                    onChangeSelect = onChangeSelect
                )
            }

            else -> {
                val binding =
                    ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return PlainViewHolder(
                    binding = binding,
                    onClick = onClick,
                    onDelete = onDelete,
                    onChangeSelect = onChangeSelect
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PlainViewHolder -> {
                holder.onBind(getItem(position))
            }

            is MultiSelectViewHolder -> {
                holder.onBind(context, getItem(position))
            }
        }
    }

    fun updateMultiSelectState(reducer: Boolean.() -> Boolean) {
        isMultiSelected.update(reducer)
    }

    class Diff : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }
}