package com.stennu718.myboardgames.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.stennu718.myboardgames.feature.chess.ChessScreen
import com.stennu718.myboardgames.feature.checkers.CheckersScreen
import com.stennu718.myboardgames.feature.sudoku.SudokuScreen
import com.stennu718.myboardgames.feature.blockudoku.BlockudokuScreen
import com.stennu718.myboardgames.feature.puzzles.PuzzlesScreen
import com.stennu718.myboardgames.feature.daily.DailyChallengeScreen
import com.stennu718.myboardgames.feature.onboarding.OnboardingScreen
import com.stennu718.myboardgames.feature.multiplayer.MultiplayerScreen
import com.stennu718.myboardgames.feature.profile.ProfileScreen
import com.stennu718.myboardgames.feature.puzzlerush.PuzzleRushScreen
import com.stennu718.myboardgames.feature.premium.PremiumScreen
import com.stennu718.myboardgames.feature.stats.StatsScreen

sealed class Screen(val route: String) {
    data object Chess : Screen("chess")
    data object Checkers : Screen("checkers")
    data object Sudoku : Screen("sudoku")
    data object Blockudoku : Screen("blockudoku")
    data object Puzzles : Screen("puzzles")
    data object Daily : Screen("daily")
    data object Profile : Screen("profile")
    data object PuzzleRush : Screen("puzzle_rush")
    data object Premium : Screen("premium")
    data object Stats : Screen("stats")
    data object Onboarding : Screen("onboarding")
    data object Multiplayer : Screen("multiplayer")
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Chess.route,
        modifier = modifier
    ) {
        composable(Screen.Chess.route) {
            ChessScreen(navController = navController)
        }
        composable(Screen.Checkers.route) {
            CheckersScreen(navController = navController)
        }
        composable(Screen.Sudoku.route) {
            SudokuScreen(navController = navController)
        }
        composable(Screen.Blockudoku.route) {
            BlockudokuScreen(navController = navController)
        }
        composable(Screen.Puzzles.route) {
            PuzzlesScreen(navController = navController)
        }
        composable(Screen.Daily.route) {
            DailyChallengeScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.PuzzleRush.route) {
            PuzzleRushScreen(navController = navController)
        }
        composable(Screen.Premium.route) {
            PremiumScreen(navController = navController)
        }
        composable(Screen.Stats.route) {
            StatsScreen(navController = navController)
        }
        composable(Screen.Onboarding.route) {
            OnboardingScreen(navController = navController)
        }
        composable(Screen.Multiplayer.route) {
            MultiplayerScreen(navController = navController)
        }
    }
}