package com.example.recipebook

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.recipebook.data.RecipeRepository
import com.example.recipebook.data.StarterRecipes
import com.example.recipebook.data.db.RecipeDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class RecipeBookApp: Application() {
    private val appScope = CoroutineScope(SupervisorJob())

    val repository: RecipeRepository by lazy {
        RecipeRepository(database)
    }

    private val starterRecipesCallback = object : RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            appScope.launch {
                StarterRecipes.insertAll(repository)
            }
        }
    }

    val database: RecipeDatabase by lazy {
        Room.databaseBuilder(this, RecipeDatabase::class.java,
            "recipes.db").addCallback(starterRecipesCallback).build()
    }

}