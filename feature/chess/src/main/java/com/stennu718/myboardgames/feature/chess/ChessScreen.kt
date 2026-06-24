package com.stennu718.myboardgames.feature.chess

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Undo
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.stennu718.myboardgames.feature.chess.engine.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChessScreen(
    navController: NavController,
    viewModel: ChessViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Chess",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Row {
                IconButton(onClick = { viewModel.undoMove() }) {
                    Icon(Icons.Default.Undo, contentDescription = "Undo")
                }
                IconButton(onClick = { viewModel.newGame() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "New Game")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Status
        if (uiState.gameStatus.isNotEmpty()) {
            Text(
                text = uiState.gameStatus,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (uiState.isThinking) {
            Text(
                text = "AI thinking...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Board
        ChessBoard(
            board = uiState.board,
            selectedSquare = uiState.selectedSquare,
            legalMoves = uiState.legalMoves,
            lastMove = uiState.lastMove,
            onSquareClick = { viewModel.onSquareClick(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Difficulty selector
        Text("Difficulty: ${uiState.difficulty}", style = MaterialTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(1 to "Easy", 2 to "Casual", 3 to "Medium", 4 to "Hard").forEach { (level, label) ->
                FilterChip(
                    selected = uiState.difficulty == level,
                    onClick = { viewModel.setDifficulty(level) },
                    label = { Text(label) }
                )
            }
        }
    }
}

@Composable
fun ChessBoard(
    board: ChessBoard,
    selectedSquare: Square?,
    legalMoves: List<Move>,
    lastMove: Move?,
    onSquareClick: (Square) -> Unit
) {
    val lightColor = Color(0xFFF0D9B5)
    val darkColor = Color(0xFFB58863)
    val selectedColor = Color(0xFF7FC97F)
    val legalMoveColor = Color(0xFF7FC97F).copy(alpha = 0.5f)
    val lastMoveColor = Color(0xCDD26A)

    val density = LocalDensity.current
    val boardSize = 320.dp
    val squareSize = boardSize / 8

    Box(modifier = Modifier.size(boardSize)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            for (row in 0..7) {
                for (col in 0..7) {
                    val square = Square(row, col)
                    val baseColor = if ((row + col) % 2 == 0) lightColor else darkColor
                    val color = when {
                        square == selectedSquare -> selectedColor
                        square == lastMove?.from || square == lastMove?.to -> lastMoveColor
                        legalMoves.any { it.to == square } -> legalMoveColor
                        else -> baseColor
                    }

                    drawRect(
                        color = color,
                        topLeft = Offset(col * squareSize.toPx(), row * squareSize.toPx()),
                        size = Size(squareSize.toPx(), squareSize.toPx())
                    )
                }
            }
        }

        // Invisible clickable overlays for each square
        for (row in 0..7) {
            for (col in 0..7) {
                val square = Square(row, col)
                Box(
                    modifier = Modifier
                        .size(squareSize)
                        .offset(
                            x = with(density) { (col * squareSize.toPx()).toDp() },
                            y = with(density) { (row * squareSize.toPx()).toDp() }
                        )
                        .clickable { onSquareClick(square) }
                )
            }
        }

        // Piece labels drawn as text overlays
        for (row in 0..7) {
            for (col in 0..7) {
                val piece = board.board[row][col] ?: continue
                val symbol = pieceSymbol(piece)
                Box(
                    modifier = Modifier
                        .size(squareSize)
                        .offset(
                            x = with(density) { (col * squareSize.toPx()).toDp() },
                            y = with(density) { (row * squareSize.toPx()).toDp() }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = symbol,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (piece.color == Color.WHITE) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

fun pieceSymbol(piece: Piece): String {
    return when (piece.type) {
        PieceType.KING -> if (piece.color == Color.WHITE) "♔" else "♚"
        PieceType.QUEEN -> if (piece.color == Color.WHITE) "♕" else "♛"
        PieceType.ROOK -> if (piece.color == Color.WHITE) "♖" else "♜"
        PieceType.BISHOP -> if (piece.color == Color.WHITE) "♗" else "♝"
        PieceType.KNIGHT -> if (piece.color == Color.WHITE) "♘" else "♞"
        PieceType.PAWN -> if (piece.color == Color.WHITE) "♙" else "♟"
    }
}