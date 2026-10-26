package com.example.recipebook.domain

import com.example.recipebook.data.model.IngredientUnit
import kotlin.math.round

object Scaling {
    fun scale(amount: Double?, unit: IngredientUnit, factor: Double): Double? {
        if (amount == null) return null

        val scaled = amount * factor

        return when (unit) {
            IngredientUnit.GRAM -> round(scaled)
            IngredientUnit.MILLILITER -> round(scaled)

            IngredientUnit.KILOGRAM -> round(scaled * 10) / 10
            IngredientUnit.LITER -> round(scaled * 10) / 10
            IngredientUnit.PIECE -> round(scaled * 10) / 10
            IngredientUnit.TABLESPOON -> round(scaled * 10) / 10
            IngredientUnit.TEASPOON -> round(scaled * 10) / 10
            IngredientUnit.TO_TASTE -> null
        }
    }
}