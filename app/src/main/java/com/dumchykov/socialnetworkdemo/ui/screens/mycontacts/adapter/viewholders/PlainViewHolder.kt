package com.dumchykov.socialnetworkdemo.ui.screens.mycontacts.adapter.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dumchykov.socialnetworkdemo.data.contactsprovider.IndicatorContact
import com.dumchykov.socialnetworkdemo.databinding.ItemContactBinding
import com.dumchykov.socialnetworkdemo.ui.util.setImageWithGlide

class PlainViewHolder(
    private val binding: ItemContactBinding,
    private val onClick: (View, IndicatorContact) -> Unit,
    private val onDelete: (IndicatorContact) -> Unit,
    private val onChangeSelect: (IndicatorContact) -> Unit = {},
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(indicatorContact: IndicatorContact) {
        binding.root.setOnClickListener {
            onClick(binding.imageMain, indicatorContact)
        }
        binding.root.setOnLongClickListener {
            onChangeSelect(indicatorContact)
            return@setOnLongClickListener false
        }
        binding.textName.text = indicatorContact.name
        binding.textCareer.text = indicatorContact.career
        binding.imageDelete.setOnClickListener {
            onDelete(indicatorContact)
        }
        binding.imageMain.setImageWithGlide("https://www.reuters.com/resizer/v2/MKQZUV67IFKAHDUNK4LJATIVMQ.jpg?auth=85a0616067eb4e93c8895d334072973babbfedb1376eb30339e6988218abc7ab")
        setTransitionName(indicatorContact)
    }

    private fun setTransitionName(indicatorContact: IndicatorContact) {
        binding.imageMain.transitionName = "${indicatorContact.id}_${indicatorContact.name}"
    }
}