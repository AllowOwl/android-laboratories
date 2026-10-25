package com.example.recipebook.data

object StarterRecipes {
    suspend fun insertAll(repository: RecipeRepository) {
        repository.saveRecipe(
            Recipe(
                title = "Блины",
                category = Category.DESSERT,
                cookingTimeMinutes = 40,
                yieldAmount = 800,
                yieldUnit = YieldUnit.GRAM,
                steps = """
                    Взбить яйца с сахаром и солью.
                    Влить молоко, всыпать муку, размешать без комков.
                    Добавить масло, дать тесту постоять 15 минут.
                    Жарить на разогретой сковороде с двух сторон.
                """.trimIndent()
            ),
            listOf(
                IngredientLine("Мука", 200.0, IngredientUnit.GRAM),
                IngredientLine("Молоко", 500.0, IngredientUnit.MILLILITER),
                IngredientLine("Яйцо", 2.0, IngredientUnit.PIECE),
                IngredientLine("Сахар", 1.0, IngredientUnit.TABLESPOON),
                IngredientLine("Соль", 0.5, IngredientUnit.TEASPOON),
                IngredientLine("Масло растительное", 2.0, IngredientUnit.TABLESPOON)
            )
        )

        repository.saveRecipe(
            Recipe(
                title = "Борщ",
                category = Category.SOUP,
                cookingTimeMinutes = 90,
                yieldAmount = 2000,
                yieldUnit = YieldUnit.MILLILITER,
                steps = """
                    Сварить бульон из говядины, 1 час.
                    Добавить нарезанный картофель и капусту.
                    Обжарить свёклу, морковь и лук с томатной пастой.
                    Переложить зажарку в бульон, варить 10 минут, посолить по вкусу.
                """.trimIndent()
            ),
            listOf(
                IngredientLine("Говядина", 400.0, IngredientUnit.GRAM),
                IngredientLine("Свёкла", 300.0, IngredientUnit.GRAM),
                IngredientLine("Картофель", 300.0, IngredientUnit.GRAM),
                IngredientLine("Капуста", 250.0, IngredientUnit.GRAM),
                IngredientLine("Морковь", 1.0, IngredientUnit.PIECE),
                IngredientLine("Лук репчатый", 1.0, IngredientUnit.PIECE),
                IngredientLine("Томатная паста", 2.0, IngredientUnit.TEASPOON),
                IngredientLine("Вода", 1.5, IngredientUnit.LITER),
                IngredientLine("Соль", null, IngredientUnit.TO_TASTE),
            )
        )

        repository.saveRecipe(
            Recipe(
                title = "Греческий салат",
                category = Category.SALAD,
                cookingTimeMinutes = 15,
                yieldAmount = 500,
                yieldUnit = YieldUnit.GRAM,
                steps = """
                    Нарезать помидоры, огурец и перец крупными кусками.
                    Добавить маслины и кубики феты.
                    Полить оливковым маслом, посолить по вкусу.
                """.trimIndent()
            ),
            listOf(
                IngredientLine("Помидоры", 2.0, IngredientUnit.PIECE),
                IngredientLine("Огурец", 1.0, IngredientUnit.PIECE),
                IngredientLine("Перец болгарский", 1.0, IngredientUnit.PIECE),
                IngredientLine("Сыр фета", 150.0, IngredientUnit.GRAM),
                IngredientLine("Маслины", 50.0, IngredientUnit.GRAM),
                IngredientLine("Масло оливковое", 2.0, IngredientUnit.TEASPOON),
                IngredientLine("Соль", null, IngredientUnit.TO_TASTE),
            )
        )
    }
}