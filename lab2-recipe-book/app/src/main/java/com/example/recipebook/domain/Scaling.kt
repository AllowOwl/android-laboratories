package com.example.recipebook.domain

import com.example.recipebook.data.model.IngredientUnit
import kotlin.math.roundToLong

object Scaling {
    fun scale(amount: Double?, unit: IngredientUnit, factor: Double): Double? {
        if (amount == null) return null

        val scaled = amount * factor

        return when (unit) {
            IngredientUnit.GRAM -> scaled.roundToLong().toDouble()
            IngredientUnit.MILLILITER -> scaled.roundToLong().toDouble()

            IngredientUnit.KILOGRAM -> (scaled * 10).roundToLong() / 10.0
            IngredientUnit.LITER -> (scaled * 10).roundToLong() / 10.0
            IngredientUnit.PIECE -> (scaled * 10).roundToLong() / 10.0
            IngredientUnit.TABLESPOON -> (scaled * 10).roundToLong() / 10.0
            IngredientUnit.TEASPOON -> (scaled * 10).roundToLong() / 10.0
            IngredientUnit.TO_TASTE -> null
        }
    }
}