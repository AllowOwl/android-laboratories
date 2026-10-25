package com.example.recipebook.data.model

data class IngredientLine(
    val name: String,
    val amount: Double?,
    val unit: IngredientUnit
)
