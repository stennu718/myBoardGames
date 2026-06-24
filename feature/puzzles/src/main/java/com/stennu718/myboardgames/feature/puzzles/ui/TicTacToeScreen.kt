package com.stennu718.myboardgames.feature.puzzles.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.stennu718.myboardgames.feature.puzzles.engine.Mark
import com.stennu718.myboardgames.feature.puzzles.engine.TicTacToe
import com.stennu718.myboardgames.feature.puzzles.engine.TicTacToeStatus
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicTacToeScreen(onBack: () -> Unit) {
    val game = remember { TicTacToe() }
    var state by remember { mutableStateOf(game.getState()) }

    LaunchedEffect(Unit) { game.newGame(); state = game.getState() }

    // AI move
    LaunchedEffect(state.currentPlayer, state.status) {
        if (state.currentPlayer == Mark.X && state.status == TicTacToeStatus.PLAYING) {
            delay(500)
            val aiMove = game.aiMove()
            if (aiMove != null) {
                game.makeMove(aiMove.first, aiMove.second)
                state = game.getState()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Tic-Tac-Toe", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            IconButton(onClick = { game.newGame(); state = game.getState() }) {
                Icon(Icons.Default.Refresh, contentDescription = "New Game")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (state.status) {
            TicTacToeStatus.X_WON -> Text("🎉 You win!", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            TicTacToeStatus.DRAW -> Text("Draw!", style = MaterialTheme.typography.titleMedium)
            TicTacToeStatus.PLAYING -> Text(
                "Your turn (You are O)",
                style = MaterialTheme.typography.titleMedium
            )
            else -> {}
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column {
            for (r in 0..2) {
                Row {
                    for (c in 0..2) {
                        val mark = state.board[r][c]
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .border(2.dp, Color.Gray)
                                .background(Color(0xFFF5F5F5))
                                .clickable {
                                    if (state.currentPlayer == Mark.X) {
                                        game.makeMove(r, c)
                                        state = game.getState()
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (mark == Mark.X) "X" else "",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1565C0)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("You are O, AI is X", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}