package com.example.recipebook.ui.editor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.recipebook.R
import com.example.recipebook.data.model.IngredientUnit
import com.example.recipebook.databinding.ItemIngredientEditBinding

class EditIngredientAdapter(
    private val lines: MutableList<EditableLine>
) : RecyclerView.Adapter<EditIngredientAdapter.LineViewHolder>() {

    private var suggestions: Array<String> = emptyArray()

    class LineViewHolder(val binding: ItemIngredientEditBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setSuggestions(names: List<String>) {
        suggestions = names.toTypedArray()
        notifyItemRangeChanged(0, itemCount)
    }

    fun addLine() {
        lines.add(EditableLine())
        notifyItemInserted(lines.size - 1)
    }

    override fun getItemCount(): Int {
        return lines.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemIngredientEditBinding.inflate(inflater, parent, false)
        val holder = LineViewHolder(binding)

        binding.nameInput.doAfterTextChanged { text ->
            lines.getOrNull(holder.bindingAdapterPosition)?.name = text.toString()
        }
        binding.amountInput.doAfterTextChanged { text ->
            lines.getOrNull(holder.bindingAdapterPosition)?.amount = text.toString()
        }
        binding.unitInput.setOnItemClickListener { _, _, index, _ ->
            val line = lines.getOrNull(holder.bindingAdapterPosition) ?: return@setOnItemClickListener
            line.unit = IngredientUnit.entries[index]
            if (line.unit == IngredientUnit.TO_TASTE) {
                binding.amountInput.setText("")
            }
            binding.amountLayout.isEnabled = line.unit != IngredientUnit.TO_TASTE
        }
        binding.removeButton.setOnClickListener {
            val position = holder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                lines.removeAt(position)
                notifyItemRemoved(position)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: LineViewHolder, position: Int) {
        val line = lines[position]
        val binding = holder.binding
        val context = binding.root.context
        val unitNames = context.resources.getStringArray(R.array.units)

        binding.nameInput.setSimpleItems(suggestions)
        binding.nameInput.setText(line.name, false)
        binding.amountInput.setText(line.amount)
        binding.unitInput.setText(unitNames[line.unit.ordinal], false)
        binding.amountLayout.isEnabled = line.unit != IngredientUnit.TO_TASTE

        binding.nameLayout.error = line.nameError?.let { context.getString(it) }
        binding.amountLayout.error = line.amountError?.let { context.getString(it) }
    }
}
