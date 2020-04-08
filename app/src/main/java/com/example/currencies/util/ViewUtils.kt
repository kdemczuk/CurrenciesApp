package com.example.currencies.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun <T, VH : RecyclerView.ViewHolder> RecyclerView.Adapter<VH>.moveItem(
    list: MutableList<T>,
    fromPosition: Int,
    toPosition: Int
) {
    if (fromPosition == toPosition) return

    val movingItem = list.removeAt(fromPosition)
    if (fromPosition < toPosition) {
        list.add(toPosition - 1, movingItem)
    } else {
        list.add(toPosition, movingItem)
    }

    notifyItemMoved(fromPosition, toPosition)
}


class TextChangedWatcher(private val afterTextChanged: (newText: String) -> Unit) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        afterTextChanged(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}