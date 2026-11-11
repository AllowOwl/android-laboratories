package com.example.recipebook.ui

import android.content.Context
import com.example.recipebook.R
import com.example.recipebook.data.model.IngredientUnit
import java.text.DecimalFormat

fun formatNumber(value: Double): String = DecimalFormat("0.##").format(value)

fun formatAmount(context: Context, amount: Double?, unit: IngredientUnit): String {
    if (amount == null) return context.getString(R.string.unit_to_taste)

    val number = formatNumber(amount)
    return when (unit) {
        IngredientUnit.GRAM -> "$number ${context.getString(R.string.unit_gram)}"
        IngredientUnit.KILOGRAM -> "$number ${context.getString(R.string.unit_kilogram)}"
        IngredientUnit.MILLILITER -> "$number ${context.getString(R.string.unit_milliliter)}"
        IngredientUnit.LITER -> "$number ${context.getString(R.string.unit_liter)}"
        IngredientUnit.PIECE -> "$number ${context.getString(R.string.unit_piece)}"

        IngredientUnit.TABLESPOON -> {
            if (amount % 1.0 == 0.0) {
                context.resources.getQuantityString(R.plurals.unit_tablespoon, amount.toInt(), number)
            } else {
                context.getString(R.string.unit_tablespoon_fraction, number)
            }
        }

        IngredientUnit.TEASPOON -> {
            if (amount % 1.0 == 0.0) {
                context.resources.getQuantityString(R.plurals.unit_teaspoon, amount.toInt(), number)
            } else {
                context.getString(R.string.unit_teaspoon_fraction, number)
            }
        }

        IngredientUnit.TO_TASTE -> context.getString(R.string.unit_to_taste)
    }
}
