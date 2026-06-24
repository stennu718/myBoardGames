package com.stennu718.myboardgames.feature.puzzles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.stennu718.myboardgames.feature.puzzles.ui.*

data class MiniGameItem(
    val name: String,
    val description: String,
    val icon: ImageVector
)

val miniGameItems = listOf(
    MiniGameItem("2048", "Slide tiles to reach 2048", Icons.Default.Numbers),
    MiniGameItem("Minesweeper", "Find the mines", Icons.Default.Explore),
    MiniGameItem("Memory", "Match card pairs", Icons.Default.Psychology),
    MiniGameItem("Tic-Tac-Toe", "Three in a row", Icons.Default.Grid3x3)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuzzlesScreen(navController: NavController) {
    var selectedGame by remember { mutableStateOf<String?>(null) }

    when (selectedGame) {
        "2048" -> Game2048Screen(onBack = { selectedGame = null })
        "Minesweeper" -> MinesweeperScreen(onBack = { selectedGame = null })
        "Memory" -> MemoryGameScreen(onBack = { selectedGame = null })
        "Tic-Tac-Toe" -> TicTacToeScreen(onBack = { selectedGame = null })
        else -> {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(
                    "Puzzle Games",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(miniGameItems) { game ->
                        Card(
                            modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                            onClick = { selectedGame = game.name }
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(game.icon, contentDescription = game.name, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(game.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(game.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}