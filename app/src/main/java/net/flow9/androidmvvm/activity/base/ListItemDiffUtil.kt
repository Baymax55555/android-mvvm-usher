package net.flow9.androidmvvm.activity.base

import androidx.recyclerview.widget.DiffUtil

open class DiffCallback<T: BaseDiffItem> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.getDiffId() == newItem.getDiffId()
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.equals(newItem)
    }
}

interface BaseDiffItem {
    fun getDiffId(): String
}