package com.dumchykov.socialnetworkdemo.ui.screens.mycontacts.adapter.viewholders

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.data.contactsprovider.IndicatorContact
import com.dumchykov.socialnetworkdemo.databinding.ItemContactMultiselectBinding
import com.dumchykov.socialnetworkdemo.ui.util.setImageWithGlide

class MultiSelectViewHolder(
    private val binding: ItemContactMultiselectBinding,
    private val onChangeSelect: (IndicatorContact) -> Unit = {},
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(context: Context, indicatorContact: IndicatorContact) {
        binding.root.setOnClickListener {
            onChangeSelect(indicatorContact)
        }
        binding.imageSelector.setImageDrawable(
            AppCompatResources.getDrawable(
                context,
                if (indicatorContact.isSelected) R.drawable.ic_selector_selected else R.drawable.ic_selector
            )
        )
        binding.textName.text = indicatorContact.name
        binding.textCareer.text = indicatorContact.career
        binding.imageMain.setImageWithGlide("https://www.reuters.com/resizer/v2/MKQZUV67IFKAHDUNK4LJATIVMQ.jpg?auth=85a0616067eb4e93c8895d334072973babbfedb1376eb30339e6988218abc7ab")
        setTransitionName(indicatorContact)
    }

    private fun setTransitionName(indicatorContact: IndicatorContact) {
        binding.imageMain.transitionName = "${indicatorContact.id}_${indicatorContact.name}"
    }
}