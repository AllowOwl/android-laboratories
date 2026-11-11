package com.example.recipebook.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipebook.data.model.IngredientLine
import com.example.recipebook.databinding.ItemIngredientBinding
import com.example.recipebook.ui.formatAmount

class IngredientAdapter : ListAdapter<IngredientLine, IngredientAdapter.IngredientViewHolder>(IngredientDiff) {

    class IngredientViewHolder(val binding: ItemIngredientBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemIngredientBinding.inflate(inflater, parent, false)
        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val line = getItem(position)
        val binding = holder.binding
        binding.ingredientName.text = line.name
        binding.ingredientAmount.text = formatAmount(binding.root.context, line.amount, line.unit)
    }

    private object IngredientDiff : DiffUtil.ItemCallback<IngredientLine>() {
        override fun areItemsTheSame(oldItem: IngredientLine, newItem: IngredientLine): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: IngredientLine, newItem: IngredientLine): Boolean {
            return oldItem == newItem
        }
    }
}
