package com.example.recipebook.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "recipe_ingredient",
    primaryKeys = ["recipeId", "ingredientId"],
    foreignKeys = [
        ForeignKey(
            entity = Recipe::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Ingredient::class,
            parentColumns = ["id"],
            childColumns = ["ingredientId"]
        )
    ],
    indices = [
        Index(value = ["ingredientId"])
    ]
)
data class RecipeIngredient(
    val recipeId: Long,
    val ingredientId: Long,
    val amount: Double?,
    val unit: IngredientUnit,
    val position: Int
)
