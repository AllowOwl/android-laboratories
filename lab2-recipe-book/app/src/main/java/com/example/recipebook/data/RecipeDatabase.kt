package com.example.recipebook.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Recipe::class, Ingredient::class, RecipeIngredient::class], version = 1,
    exportSchema = false)
abstract class RecipeDatabase: RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): IngredientDao
}
