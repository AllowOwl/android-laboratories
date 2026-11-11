package com.example.recipebook.domain

object IngredientNames {
    fun normalize(raw: String): String {
        return raw.trim()
            .replace(Regex("\\s+"), " ").lowercase()
            .replaceFirstChar { it.uppercase() }
    }
}