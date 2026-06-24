package com.stennu718.myboardgames.feature.puzzles.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stennu718.myboardgames.feature.puzzles.engine.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MinesweeperScreen(onBack: () -> Unit) {
    val game = remember { MinesweeperGame() }
    var state by remember { mutableStateOf(game.getState()) }

    LaunchedEffect(Unit) { game.newGame(); state = game.getState() }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Minesweeper", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            IconButton(onClick = { game.newGame(); state = game.getState() }) {
                Icon(Icons.Default.Refresh, contentDescription = "New Game")
            }
        }

        Text("Mines: ${state.mineCount - state.flagCount}", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        when (state.status) {
            MineGameStatus.WON -> Text("🎉 You win!", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            MineGameStatus.LOST -> Text("💥 Game Over", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.error)
            else -> {}
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column {
            for (r in 0 until state.rows) {
                Row {
                    for (c in 0 until state.cols) {
                        val cell = state.grid[r][c]
                        val bgColor = when (cell.state) {
                            CellState.HIDDEN -> Color(0xFFBDBDBD)
                            CellState.FLAGGED -> Color(0xFFFFCDD2)
                            CellState.REVEALED -> Color(0xFFE0E0E0)
                        }

                        val text = when (cell.state) {
                            CellState.FLAGGED -> "🚩"
                            CellState.REVEALED && cell.hasMine -> "💣"
                            CellState.REVEALED && cell.neighborMines > 0 -> cell.neighborMines.toString()
                            else -> ""
                        }

                        val textColor = when (cell.neighborMines) {
                            1 -> Color.Blue
                            2 -> Color.Green
                            3 -> Color.Red
                            4 -> Color(0xFF000080)
                            5 -> Color(0xFF800000)
                            6 -> Color.Cyan
                            7 -> Color.Black
                            8 -> Color.Gray
                            else -> Color.Black
                        }

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(bgColor)
                                .combinedClickable(
                                    onClick = { game.reveal(r, c); state = game.getState() },
                                    onLongClick = { game.toggleFlag(r, c); state = game.getState() }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("Tap to reveal, long-press to flag", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}