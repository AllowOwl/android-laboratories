package com.example.recipebook.ui

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.recipebook.R
import com.example.recipebook.data.model.Category
import com.example.recipebook.data.model.YieldUnit

data class CategoryAppearance(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @ColorRes val container: Int,
    @ColorRes val onContainer: Int
)

fun Category.appearance(): CategoryAppearance = when (this) {
    Category.SOUP -> CategoryAppearance(R.string.category_soup, R.drawable.ic_category_soup,
        R.color.category_soup_container, R.color.category_soup_on_container)

    Category.SALAD -> CategoryAppearance(R.string.category_salad, R.drawable.ic_category_salad,
        R.color.category_salad_container, R.color.category_salad_on_container)

    Category.DESSERT -> CategoryAppearance(R.string.category_dessert, R.drawable.ic_category_dessert,
        R.color.category_dessert_container, R.color.category_dessert_on_container)

    Category.DRINK -> CategoryAppearance(R.string.category_drink, R.drawable.ic_category_drink,
        R.color.category_drink_container, R.color.category_drink_on_container)

    Category.SNACK -> CategoryAppearance(R.string.category_snack, R.drawable.ic_category_snack,
        R.color.category_snack_container, R.color.category_snack_on_container)

    Category.MAIN_DISH -> CategoryAppearance(R.string.category_main_dish, R.drawable.ic_category_main_dish,
        R.color.category_main_dish_container, R.color.category_main_dish_on_container)

    Category.SIDE_DISH -> CategoryAppearance(R.string.category_side_dish, R.drawable.ic_category_side_dish,
        R.color.category_side_dish_container, R.color.category_side_dish_on_container)
}

@StringRes
fun YieldUnit.label(): Int = when (this) {
    YieldUnit.GRAM -> R.string.unit_gram
    YieldUnit.MILLILITER -> R.string.unit_milliliter
}
