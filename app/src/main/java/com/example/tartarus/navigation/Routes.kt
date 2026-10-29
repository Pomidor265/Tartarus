package com.example.tartarus.navigation

object Routes {
    const val LOGIN            = "login"
    const val REGISTER         = "register"
    const val PROFILE          = "profile"
    const val CATEGORIES       = "categories"
    const val CREATURES        = "creatures/{categoryId}"
    const val CREATURE         = "creature/{creatureId}"
    const val BOOKMARKS        = "bookmarks"
    const val BOOKMARK_DETAIL  = "bookmark/{bookmarkId}/{bookmarkName}"
    const val DIFFICULTY = "difficulty"
    const val QUIZ = "quiz/{difficultyId}/{questionsCount}"

    fun quiz(difficultyId: Long, questionsCount: Int) = "quiz/$difficultyId/$questionsCount"

    fun creatures(categoryId: Long) = "creatures/$categoryId"
    fun creature(creatureId: Long)  = "creature/$creatureId"

    fun bookmarkDetail(id: Long?, name: String): String {
        val encodedName = java.net.URLEncoder.encode(name, "UTF-8")
        return "bookmark/$id/$encodedName"
    }
}