package com.dumchykov.socialnetworkdemo.ui.screens.addcontacts.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dumchykov.socialnetworkdemo.data.contactsprovider.IndicatorContact
import com.dumchykov.socialnetworkdemo.databinding.ItemContactAddBinding
import com.dumchykov.socialnetworkdemo.ui.util.PROFILE_IMAGE_URL
import com.dumchykov.socialnetworkdemo.ui.util.setImageWithGlide

class AddContactsViewHolder(
    private val binding: ItemContactAddBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(
        indicatorContact: IndicatorContact,
        onClickListener: (Int, String) -> Unit,
        onAddListener: (IndicatorContact) -> Unit,
    ) {
        binding.imageMain.setImageWithGlide(PROFILE_IMAGE_URL)
        binding.textName.text = indicatorContact.name
        binding.textCareer.text = indicatorContact.career

        binding.root.setOnClickListener {
            onClickListener(indicatorContact.id, indicatorContact.name)
        }

        if (indicatorContact.isAdded) {
            binding.layoutAddContact.visibility = View.GONE
            binding.imageAdded.visibility = View.VISIBLE
        } else {
            binding.layoutAddContact.visibility = View.VISIBLE
            binding.imageAdded.visibility = View.GONE

            binding.layoutAddContact.setOnClickListener {
                onAddListener(indicatorContact)
            }
        }
    }
}