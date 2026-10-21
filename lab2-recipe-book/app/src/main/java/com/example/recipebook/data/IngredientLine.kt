package com.example.recipebook.data

data class IngredientLine(
    val name: String,
    val amount: Double?,
    val unit: IngredientUnit,
    val position: Int
)
