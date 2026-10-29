package com.example.recipebook.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipebook.data.model.Ingredient
import com.example.recipebook.data.model.Recipe
import com.example.recipebook.data.model.RecipeIngredient

@Database(entities = [Recipe::class, Ingredient::class, RecipeIngredient::class], version = 1,
    exportSchema = false)
abstract class RecipeDatabase: RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): IngredientDao
}
