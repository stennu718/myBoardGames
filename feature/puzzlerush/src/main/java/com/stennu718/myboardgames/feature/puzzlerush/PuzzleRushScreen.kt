package com.stennu718.myboardgames.feature.puzzlerush

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.stennu718.myboardgames.feature.chess.engine.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuzzleRushScreen(
    navController: NavController,
    viewModel: PuzzleRushViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val chessBoard by viewModel.chessBoard.collectAsStateWithLifecycle()
    val selectedSquare by viewModel.selectedSquare.collectAsStateWithLifecycle()
    val legalMoves by viewModel.legalMoves.collectAsStateWithLifecycle()

    // Auto-tick the timer
    LaunchedEffect(uiState.isActive) {
        while (uiState.isActive) {
            delay(100)
            viewModel.tick(100)
        }
    }

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
            // Chess board with proper interaction
            PuzzleChessBoard(
                board = chessBoard,
                selectedSquare = selectedSquare,
                legalMoves = legalMoves,
                lastMove = null,
                onSquareClick = { square -> viewModel.onSquareClick(square) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Show current puzzle hint
            uiState.currentPuzzle?.let { puzzle ->
                Text(
                    "Theme: ${puzzle.theme.replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun PuzzleChessBoard(
    board: ChessBoard,
    selectedSquare: Square?,
    legalMoves: List<Move>,
    lastMove: Move?,
    onSquareClick: (Square) -> Unit
) {
    val lightColor = Color(0xFFF0D9B5)
    val darkColor = Color(0xFFB58863)
    val selectedColor = Color(0xFF7FC97F)
    val legalMoveColor = Color(0x8066BB6A)
    val lastMoveColor = Color(0x55AAAAAA)

    val density = LocalDensity.current
    val boardSize = 320.dp
    val squareSize = boardSize / 8

    Canvas(
        modifier = Modifier.size(boardSize).clickable { }
    ) {
        val sqSize = size.width / 8

        for (row in 0..7) {
            for (col in 0..7) {
                val square = Square(row, col)
                val x = col * sqSize
                val y = row * sqSize

                // Determine square color
                val baseColor = if ((row + col) % 2 == 0) lightColor else darkColor
                val color = when {
                    square == selectedSquare -> selectedColor
                    square == lastMove?.from || square == lastMove?.to -> lastMoveColor
                    else -> baseColor
                }
                drawRect(color = color, topLeft = Offset(x, y), size = Size(sqSize, sqSize))

                // Draw legal move indicators
                if (legalMoves.any { it.to == square }) {
                    val centerX = x + sqSize / 2
                    val centerY = y + sqSize / 2
                    val hasCapture = legalMoves.any { it.to == square && it.captured != null }
                    if (hasCapture) {
                        drawCircle(color = legalMoveColor, radius = sqSize * 0.45f, center = Offset(centerX, centerY))
                    } else {
                        drawCircle(color = legalMoveColor, radius = sqSize * 0.15f, center = Offset(centerX, centerY))
                    }
                }

                // Draw piece
                val piece = board.getPiece(square)
                if (piece != null) {
                    drawPiece(piece, x, y, sqSize)
                }
            }
        }
    }

    // Invisible clickable overlay for touch detection
    Box(modifier = Modifier.size(boardSize)) {
        for (row in 0..7) {
            for (col in 0..7) {
                val square = Square(row, col)
                Box(
                    modifier = Modifier
                        .size(squareSize)
                        .offset(x = (col * squareSize.value).dp, y = (row * squareSize.value).dp)
                        .clickable { onSquareClick(square) }
                )
            }
        }
    }
}

private fun DrawScope.drawPiece(piece: Piece, x: Float, y: Float, size: Float) {
    val symbol = when (piece.type) {
        PieceType.KING -> if (piece.color == Color.WHITE) "♔" else "♚"
        PieceType.QUEEN -> if (piece.color == Color.WHITE) "♕" else "♛"
        PieceType.ROOK -> if (piece.color == Color.WHITE) "♖" else "♜"
        PieceType.BISHOP -> if (piece.color == Color.WHITE) "♗" else "♝"
        PieceType.KNIGHT -> if (piece.color == Color.WHITE) "♘" else "♞"
        PieceType.PAWN -> if (piece.color == Color.WHITE) "♙" else "♟"
    }
    drawContext.canvas.nativeCanvas.drawText(
        symbol,
        x + size / 2,
        y + size * 0.72f,
        android.graphics.Paint().apply {
            textSize = size * 0.65f
            textAlign = android.graphics.Paint.Align.CENTER
            isAntiAlias = true
            color = if (piece.color == Color.WHITE) android.graphics.Color.WHITE else android.graphics.Color.BLACK
            setShadowLayer(2f, 1f, 1f, android.graphics.Color.BLACK)
        }
    )
}

@Composable
fun StatChip(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}