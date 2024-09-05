package com.dumchykov.socialnetworkdemo.ui.screens.mycontacts.adapter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dumchykov.socialnetworkdemo.R

class ContactsItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildLayoutPosition(view)
        val offset8 = context.resources.getDimensionPixelSize(R.dimen._8dp)
        val offset16 = context.resources.getDimensionPixelSize(R.dimen._16dp)
        when (position) {
            0 -> {
                outRect.set(offset16, offset8, offset16, offset8)
            }

            else -> {
                outRect.set(offset16, 0, offset16, offset8)
            }
        }
    }
}