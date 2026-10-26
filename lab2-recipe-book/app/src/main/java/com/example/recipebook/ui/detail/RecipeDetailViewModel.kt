package com.example.recipebook.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.recipebook.RecipeBookApp
import com.example.recipebook.data.model.IngredientLine
import com.example.recipebook.data.model.Recipe
import com.example.recipebook.domain.Scaling
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class RecipeDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository = (application as RecipeBookApp).repository

    private val recipeId: Long = savedStateHandle[RecipeDetailActivity.EXTRA_RECIPE_ID] ?: 0L

    private val _factor = MutableStateFlow(1.0)
    val factor: StateFlow<Double> = _factor

    val recipe: Flow<Recipe?> = repository.observeRecipe(recipeId)

    val lines: Flow<List<IngredientLine>> =
        combine(repository.observeLines(recipeId), _factor) { original, k ->
            original.map { line ->
                line.copy(amount = Scaling.scale(line.amount, line.unit, k))
            }
        }

    fun scaleTo(wantedYield: Double, originalYield: Int) {
        _factor.value = wantedYield / originalYield
    }

    fun resetScale() {
        _factor.value = 1.0
    }

    fun setFavorite(favorite: Boolean) {
        viewModelScope.launch {
            repository.setFavorite(recipeId, favorite)
        }
    }
}
