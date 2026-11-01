package com.example.tartarus.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tartarus.data.Difficulty
import com.example.tartarus.screens.BookmarkDetailScreen
import com.example.tartarus.screens.BookmarksScreen
import com.example.tartarus.screens.CategoryScreen
import com.example.tartarus.screens.CreatureDetailScreen
import com.example.tartarus.screens.CreaturesScreen
import com.example.tartarus.screens.DifficultyScreen
import com.example.tartarus.screens.LoginScreen
import com.example.tartarus.screens.ProfileScreen
import com.example.tartarus.screens.QuizScreen
import com.example.tartarus.screens.RegisterScreen

@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.CATEGORIES
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.CATEGORIES) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoToRegister = {
                    navController.navigate(Routes.REGISTER)
                },
                viewModel = viewModel()
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.CATEGORIES) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoToLogin = {
                    navController.popBackStack()
                },
                viewModel = viewModel()
            )
        }

        composable(Routes.CATEGORIES) {
            CategoryScreen(
                onCategoryClick = { category ->
                    navController.navigate(Routes.creatures(category.id))
                },

                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                },
                onBookmarkClick = {navController.navigate((Routes.BOOKMARKS))},
                onGameClick = { navController.navigate(Routes.DIFFICULTY) },
                viewModel = viewModel()
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                onHomeClick = { navController.navigate(Routes.CATEGORIES)},
                onGoToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBookmarkClick = {navController.navigate((Routes.BOOKMARKS))},
                onGameClick = {navController.navigate(Routes.DIFFICULTY)},
                viewModel = viewModel()

            )
        }

        composable(
            route = Routes.CREATURES,
            arguments = listOf(navArgument("categoryId") { type = NavType.LongType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getLong("categoryId") ?: 0L
            CreaturesScreen(
                categoryId = categoryId,
                onCreatureClick = { creature ->
                    navController.navigate(Routes.creature(creature.id))
                },
                onExtraClick = { navController.popBackStack() },
                onHomeClick = {navController.navigate(Routes.CATEGORIES)},
                onProfileClick = {navController.navigate(Routes.PROFILE)},
                onBookmarkClick = {navController.navigate((Routes.BOOKMARKS))},
                onGameClick = {navController.navigate(Routes.DIFFICULTY)}
            )
        }

        composable(
            route = Routes.CREATURE,
            arguments = listOf(navArgument("creatureId") { type = NavType.LongType })
        ) { backStackEntry ->
            val creatureId = backStackEntry.arguments?.getLong("creatureId") ?: 0L
            CreatureDetailScreen(
                creatureId = creatureId,
                onExtraClick = { navController.popBackStack() },
                onHomeClick = {navController.navigate(Routes.CATEGORIES)},
                onProfileClick = {navController.navigate(Routes.PROFILE)},
                onBookmarkClick = {navController.navigate((Routes.BOOKMARKS))},
                onGameClick = {navController.navigate(Routes.DIFFICULTY)}
            )
        }
        composable(Routes.BOOKMARKS) {
            BookmarksScreen(
                onBookmarkClick = { bm ->
                    navController.navigate(Routes.bookmarkDetail(bm.id, bm.name))
                },
                onHomeClick = { navController.navigate(Routes.CATEGORIES) { popUpTo(0) } },
                onProfileClick = { navController.navigate(Routes.PROFILE) },
                onGameClick = {navController.navigate(Routes.DIFFICULTY)}
            )
        }

        composable(
            route = Routes.BOOKMARK_DETAIL,
            arguments = listOf(
                navArgument("bookmarkId") { type = NavType.LongType },
                navArgument("bookmarkName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("bookmarkId") ?: 0L
            val name = backStackEntry.arguments?.getString("bookmarkName").orEmpty()
            BookmarkDetailScreen(
                bookmarkId = id,
                bookmarkName = name,
                onCreatureClick = { c -> navController.navigate(Routes.creature(c.id)) },
                onExtraClick = { navController.popBackStack() },
                onHomeClick = { navController.navigate(Routes.CATEGORIES) { popUpTo(0) } },
                onProfileClick = { navController.navigate(Routes.PROFILE) },
                onGameClick = {navController.navigate(Routes.DIFFICULTY)},
                onBookmarkClick = {navController.navigate(Routes.BOOKMARKS)}
            )
        }

        composable(Routes.DIFFICULTY) {
            DifficultyScreen(
                onDifficultySelected = { diff ->
                    navController.navigate(Routes.quiz(diff.id, diff.questionsCount))
                },
                onHomeClick = { navController.navigate(Routes.CATEGORIES) { popUpTo(0) } },
                onBookmarkClick = { navController.navigate(Routes.BOOKMARKS) },
                onProfileClick = { navController.navigate(Routes.PROFILE) }
            )
        }

        composable(
            route = Routes.QUIZ,
            arguments = listOf(
                navArgument("difficultyId") { type = NavType.LongType },
                navArgument("questionsCount") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val diffId = backStackEntry.arguments?.getLong("difficultyId") ?: 0L
            val qCount = backStackEntry.arguments?.getInt("questionsCount") ?: 0
            val diffName = "Уровень $diffId" // Можно передать имя или подгрузить внутри

            QuizScreen(
                difficulty = Difficulty(id = diffId, name = diffName, questionsCount = qCount),
                onQuizFinished = {
                    navController.popBackStack()
                },
                onHomeClick = { navController.navigate(Routes.CATEGORIES) { popUpTo(0) } },
                onBookmarkClick = { navController.navigate(Routes.BOOKMARKS) },
                onProfileClick = { navController.navigate(Routes.PROFILE) }
            )
        }

    }
}