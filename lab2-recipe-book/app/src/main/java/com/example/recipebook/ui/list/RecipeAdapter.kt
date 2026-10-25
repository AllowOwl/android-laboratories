package com.example.recipebook.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipebook.R
import com.example.recipebook.data.model.Recipe
import com.example.recipebook.databinding.ItemRecipeBinding
import com.example.recipebook.ui.appearance
import com.example.recipebook.ui.label

class RecipeAdapter(
    private val onRecipeClick: (Long) -> Unit
) : ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(RecipeDiff) {

    class RecipeViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        val binding = holder.binding
        val context = binding.root.context
        val appearance = recipe.category.appearance()

        binding.recipeTitle.text = recipe.title
        binding.recipeMeta.text = listOf(
            context.getString(appearance.title),
            context.getString(R.string.format_minutes, recipe.cookingTimeMinutes),
            "${recipe.yieldAmount} ${context.getString(recipe.yieldUnit.label())}"
        ).joinToString(" · ")

        binding.categoryIcon.setImageResource(appearance.icon)
        binding.categoryIcon.backgroundTintList =
            ContextCompat.getColorStateList(context, appearance.container)
        binding.categoryIcon.imageTintList =
            ContextCompat.getColorStateList(context, appearance.onContainer)

        binding.favoriteIcon.isVisible = recipe.isFavorite

        binding.root.setOnClickListener {
            onRecipeClick(recipe.id)
        }
    }

    private object RecipeDiff : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(
            oldItem: Recipe,
            newItem: Recipe
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Recipe,
            newItem: Recipe
        ): Boolean {
            return oldItem == newItem
        }
    }
}
