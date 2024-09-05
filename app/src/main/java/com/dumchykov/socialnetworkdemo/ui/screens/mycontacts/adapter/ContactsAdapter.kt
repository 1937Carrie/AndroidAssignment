package com.dumchykov.socialnetworkdemo.ui.screens.mycontacts.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dumchykov.socialnetworkdemo.data.contactsprovider.IndicatorContact
import com.dumchykov.socialnetworkdemo.databinding.ItemContactBinding
import com.dumchykov.socialnetworkdemo.databinding.ItemContactMultiselectBinding
import com.dumchykov.socialnetworkdemo.ui.screens.mycontacts.adapter.viewholders.MultiSelectViewHolder
import com.dumchykov.socialnetworkdemo.ui.screens.mycontacts.adapter.viewholders.PlainViewHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ContactsAdapter(
    private val context: Context,
    private val onClick: (View, IndicatorContact) -> Unit = { _, _ -> },
    private val onDelete: (IndicatorContact) -> Unit = {},
    private val onChangeSelect: (IndicatorContact) -> Unit = {},
) : ListAdapter<IndicatorContact, RecyclerView.ViewHolder>(Diff()) {
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

    class Diff : DiffUtil.ItemCallback<IndicatorContact>() {
        override fun areItemsTheSame(
            oldItem: IndicatorContact,
            newItem: IndicatorContact,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: IndicatorContact,
            newItem: IndicatorContact,
        ): Boolean {
            return oldItem == newItem
        }
    }
}