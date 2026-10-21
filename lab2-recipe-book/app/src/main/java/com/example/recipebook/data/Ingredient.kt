package com.example.recipebook.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "ingredient", indices = [Index(value = ["name"], unique = true)])
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)
