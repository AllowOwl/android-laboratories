package com.example.recipebook.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: Category,
    val cookingTimeMinutes: Int,
    val yieldAmount: Int,
    val yieldUnit: YieldUnit,
    val steps: String,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)