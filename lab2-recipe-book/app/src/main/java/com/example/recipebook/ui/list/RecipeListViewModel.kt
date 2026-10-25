package com.example.recipebook.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.recipebook.RecipeBookApp
import com.example.recipebook.data.model.Category
import com.example.recipebook.data.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class RecipeListViewModel(application: Application): AndroidViewModel(application) {
    private val repository = (application as RecipeBookApp).repository

    private val query = MutableStateFlow("")
    private val category = MutableStateFlow<Category?>(null)
    private val favoritesOnly = MutableStateFlow(false)

    val recipes: Flow<List<Recipe>> = combine(
        repository.observeRecipes(), query, category, favoritesOnly
    ) { all, text, selectedCategory, onlyFavorites ->
        all.filter { recipe ->
            val matchesSearch = recipe.title.contains(text.trim(), ignoreCase = true)
            val matchesCategory = selectedCategory == null || recipe.category == selectedCategory
            val matchesFavorites = !onlyFavorites || recipe.isFavorite

            matchesSearch && matchesCategory && matchesFavorites
        }
    }

    fun setQuery(text: String) {
        query.value = text
    }

    fun showAll() {
        category.value = null
        favoritesOnly.value = false
    }

    fun showFavorites() {
        category.value = null
        favoritesOnly.value = true
    }

    fun showCategory(selected: Category) {
        category.value = selected
        favoritesOnly.value = false
    }

}