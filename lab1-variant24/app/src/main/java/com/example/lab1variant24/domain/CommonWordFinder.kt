package com.example.lab1variant24.domain

object CommonWordFinder {
    const val FIRST_SENTENCE = "One two three four five six seven"
    const val SECOND_SENTENCE = "Three friends to find a two cockroaches"

    fun findLongestCommonWords(first: String, second: String): List<String> {
        val commonWords = extractWords(first) intersect extractWords(second)
        val maxLength = commonWords.maxOfOrNull { it.length } ?: return emptyList()
        return commonWords
            .filter { it.length == maxLength }
            .sorted()
    }

    private fun extractWords(sentence: String): Set<String> {
        return sentence
            .lowercase()
            .split(" ")
            .filter { it.isNotEmpty() }
            .toSet()
    }
}