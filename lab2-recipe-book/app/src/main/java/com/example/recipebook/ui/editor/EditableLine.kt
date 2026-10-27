package com.example.recipebook.ui.editor

import androidx.annotation.StringRes
import com.example.recipebook.data.model.IngredientUnit

class EditableLine(
    var name: String = "",
    var amount: String = "",
    var unit: IngredientUnit = IngredientUnit.GRAM
) {
    @StringRes var nameError: Int? = null
    @StringRes var amountError: Int? = null
}
