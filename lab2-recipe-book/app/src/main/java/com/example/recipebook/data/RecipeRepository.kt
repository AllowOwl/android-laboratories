package com.example.recipebook.data

import androidx.room.withTransaction
import com.example.recipebook.domain.IngredientNames
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val database: RecipeDatabase) {
    private val recipeDao = database.recipeDao()
    private val ingredientDao = database.ingredientDao()

    fun observeRecipes(): Flow<List<Recipe>> {
        return recipeDao.observeAll()
    }

    fun observeRecipe(id: Long): Flow<Recipe?> {
        return recipeDao.observeById(id)
    }

    fun observeLines(recipeId: Long): Flow<List<IngredientLine>> {
        return recipeDao.observeLines(recipeId)
    }

    fun observeIngredientNames(): Flow<List<String>> {
        return ingredientDao.observeNames()
    }

    suspend fun getRecipe(id: Long): Recipe? {
        return recipeDao.getById(id)
    }

    suspend fun getLines(recipeId: Long): List<IngredientLine> {
        return recipeDao.getLines(recipeId)
    }

    suspend fun setFavorite(id: Long, favorite: Boolean) {
        recipeDao.setFavorite(id, favorite)
    }

    suspend fun deleteRecipe(id: Long) {
        recipeDao.deleteById(id)
    }

    suspend fun saveRecipe(recipe: Recipe, lines: List<IngredientLine>): Long {
        return database.withTransaction {
            val recipeId = if (recipe.id == 0L) {
                recipeDao.insert(recipe)
            } else {
                recipeDao.update(recipe)
                recipeDao.deleteLinks(recipe.id)
                recipe.id
            }

            val links = lines.mapIndexed { index, line ->
                RecipeIngredient(
                    recipeId = recipeId,
                    ingredientId = findOrCreateIngredient(line.name),
                    amount = line.amount,
                    unit = line.unit,
                    position = index
                )
            }
            recipeDao.insertLinks(links)

            recipeId
        }
    }

    private suspend fun findOrCreateIngredient(rawName: String): Long {
        val name = IngredientNames.normalize(rawName)

        val existing = ingredientDao.findByName(name)
        if (existing != null) {
            return existing.id
        }

        return ingredientDao.insert(Ingredient(name = name))
    }
}