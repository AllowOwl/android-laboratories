package com.example.recipebook.ui.editor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.example.recipebook.R
import com.example.recipebook.RecipeBookApp
import com.example.recipebook.data.model.Category
import com.example.recipebook.data.model.IngredientLine
import com.example.recipebook.data.model.IngredientUnit
import com.example.recipebook.data.model.Recipe
import com.example.recipebook.data.model.YieldUnit
import com.example.recipebook.domain.IngredientNames
import com.example.recipebook.ui.formatNumber
import kotlinx.coroutines.flow.Flow

class EditRecipeViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository = (application as RecipeBookApp).repository

    private val recipeId: Long = savedStateHandle[EditRecipeActivity.EXTRA_RECIPE_ID] ?: 0L

    private var original: Recipe? = null

    val isNew: Boolean = recipeId == 0L
    val lines = mutableListOf<EditableLine>()
    var category: Category? = null

    var isLoaded = false
        private set

    val ingredientNames: Flow<List<String>> = repository.observeIngredientNames()

    suspend fun load(): Recipe? {
        val recipe = if (isNew) null else repository.getRecipe(recipeId)
        val loadedLines = if (isNew) emptyList() else repository.getLines(recipeId)

        original = recipe
        category = recipe?.category
        lines.clear()
        for (line in loadedLines) {
            val amount = if (line.amount == null) "" else formatNumber(line.amount)
            lines.add(EditableLine(line.name, amount, line.unit))
        }
        if (lines.isEmpty()) {
            lines.add(EditableLine())
        }
        isLoaded = true
        return recipe
    }

    fun checkLines(): Boolean {
        val names = mutableSetOf<String>()
        var ok = true
        for (line in lines) {
            line.nameError = null
            line.amountError = null
            if (line.name.isBlank()) {
                if (line.amount.isNotBlank()) {
                    line.nameError = R.string.error_name_required
                    ok = false
                }
                continue
            }
            if (!names.add(IngredientNames.normalize(line.name))) {
                line.nameError = R.string.error_ingredient_duplicate
                ok = false
            }
            if (line.unit != IngredientUnit.TO_TASTE && parseAmount(line.amount) == null) {
                line.amountError = R.string.error_number
                ok = false
            }
        }
        return ok
    }

    suspend fun save(
        title: String,
        category: Category,
        cookingTimeMinutes: Int,
        yieldAmount: Int,
        yieldUnit: YieldUnit,
        steps: String
    ) {
        val old = original
        val recipe = Recipe(
            id = recipeId,
            title = title,
            category = category,
            cookingTimeMinutes = cookingTimeMinutes,
            yieldAmount = yieldAmount,
            yieldUnit = yieldUnit,
            steps = steps,
            isFavorite = old?.isFavorite ?: false,
            createdAt = old?.createdAt ?: System.currentTimeMillis()
        )
        val filled = lines.filter { it.name.isNotBlank() }.map { line ->
            val amount = if (line.unit == IngredientUnit.TO_TASTE) null else parseAmount(line.amount)
            IngredientLine(line.name, amount, line.unit)
        }
        repository.saveRecipe(recipe, filled)
    }

    suspend fun delete() {
        repository.deleteRecipe(recipeId)
    }

    private fun parseAmount(text: String): Double? {
        return text.replace(',', '.').toDoubleOrNull()?.takeIf { it > 0 }
    }
}
