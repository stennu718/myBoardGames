package com.stennu718.myboardgames.feature.puzzlerush

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.stennu718.myboardgames.feature.chess.ChessBoard
import com.stennu718.myboardgames.feature.chess.engine.*
import com.stennu718.myboardgames.feature.chess.pieceSymbol

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuzzleRushScreen(
    navController: NavController,
    viewModel: PuzzleRushViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val chessBoard by viewModel.chessBoard.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.startGame() }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with timer and score
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Puzzle Rush", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⏱ ${uiState.timeRemainingMs / 1000}s", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    color = if (uiState.timeRemainingMs < 30000) Color.Red else MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.width(16.dp))
                Text("★ ${uiState.score}", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Stats row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatChip("Solved", uiState.puzzlesSolved.toString(), Color(0xFF4CAF50))
            StatChip("Failed", uiState.puzzlesFailed.toString(), Color(0xFFF44336))
            StatChip("Streak", uiState.streak.toString(), Color(0xFFFF9800))
            StatChip("Rating", uiState.rating.toString(), Color(0xFF2196F3))
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!uiState.isActive && uiState.puzzlesSolved + uiState.puzzlesFailed > 0) {
            // Game over screen
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("⏰ Time's Up!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Score: ${uiState.score}", style = MaterialTheme.typography.titleLarge)
                    Text("Solved: ${uiState.puzzlesSolved}/${uiState.puzzlesSolved + uiState.puzzlesFailed}")
                    Text("Best Streak: ${uiState.bestStreak}")
                    Text("Rating: ${uiState.rating}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.startGame() }) {
                        Text("Play Again")
                    }
                }
            }
        } else {
            // Chess board
            ChessBoard(
                board = chessBoard,
                selectedSquare = null,
                legalMoves = emptyList(),
                lastMove = null,
                onSquareClick = { square ->
                    val piece = chessBoard.getPiece(square)
                    if (piece != null && piece.color == chessBoard.turn) {
                        // Find legal moves for this piece
                        val gen = MoveGenerator(chessBoard)
                        val moves = gen.generateLegalMoves().filter { it.from == square }
                        if (moves.isNotEmpty()) {
                            // Auto-select first legal move for simplicity
                            viewModel.onMove(moves.first())
                        }
                    } else {
                        // Try to make a move to this square
                        val gen = MoveGenerator(chessBoard)
                        val moves = gen.generateLegalMoves().filter { it.to == square }
                        if (moves.isNotEmpty()) {
                            viewModel.onMove(moves.first())
                        }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Theme stats
        val themeStats = viewModel.engine.getThemeStats(uiState)
        if (themeStats.isNotEmpty()) {
            Text("Themes:", style = MaterialTheme.typography.bodySmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                themeStats.forEach { (theme, stats) ->
                    Text("$theme: ${stats.first}/${stats.first + stats.second}",
                        style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun StatChip(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}
