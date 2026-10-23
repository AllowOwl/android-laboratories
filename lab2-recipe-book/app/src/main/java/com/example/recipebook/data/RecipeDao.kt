package com.example.recipebook.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe ORDER BY title")
    fun observeAll(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE id = :id")
    fun observeById(id: Long): Flow<Recipe?>

    @Query("SELECT * FROM recipe WHERE id = :id")
    suspend fun getById(id: Long): Recipe?

    @Query("""
        SELECT ing.name, ri.amount, ri.unit, ri.position
        FROM recipe_ingredient ri JOIN ingredient ing ON ing.id = ri.ingredientId
        WHERE ri.recipeId = :recipeId ORDER BY ri.position
    """)
    fun observeLines(recipeId: Long): Flow<List<IngredientLine>>

    @Query("""
        SELECT ing.name, ri.amount, ri.unit, ri.position
        FROM recipe_ingredient ri JOIN ingredient ing ON ing.id = ri.ingredientId
        WHERE ri.recipeId = :recipeId ORDER BY ri.position
    """)
    suspend fun getLines(recipeId: Long): List<IngredientLine>

    @Insert
    suspend fun insert(recipe: Recipe): Long

    @Update
    suspend fun update(recipe: Recipe)

    @Query("DELETE FROM recipe WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE recipe SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    @Insert
    suspend fun insertLinks(links: List<RecipeIngredient>)

    @Query("DELETE FROM recipe_ingredient WHERE recipeId = :recipeId")
    suspend fun deleteLinks(recipeId: Long)
}
