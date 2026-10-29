package com.example.recipebook.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.recipebook.data.model.Ingredient
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredient WHERE name = :name")
    suspend fun findByName(name: String): Ingredient?

    @Insert
    suspend fun insert(ingredient: Ingredient): Long

    @Query("SELECT name FROM ingredient ORDER BY name")
    fun observeNames(): Flow<List<String>>
}