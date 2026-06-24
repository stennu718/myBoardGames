package com.stennu718.myboardgames.feature.checkers

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckersScreen(
    navController: NavController,
    viewModel: CheckersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Checkers", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            IconButton(onClick = { viewModel.newGame() }) {
                Icon(Icons.Default.Refresh, contentDescription = "New Game")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.gameStatus.isNotEmpty()) {
            Text(uiState.gameStatus, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Board
        val boardColor = Color(0xFF8B4513)
        val lightColor = Color(0xFFDEB887)
        val redColor = Color(0xFFCC0000)
        val blackColor = Color(0xFF333333)
        val selectedColor = Color(0xFF7FC97F)
        val validMoveColor = Color(0xFF7FC97F).copy(alpha = 0.5f)

        Box(modifier = Modifier.size(320.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val squareSize = size.width / 8
                for (row in 0..7) {
                    for (col in 0..7) {
                        val baseColor = if ((row + col) % 2 == 0) lightColor else boardColor
                        val isSelected = row == uiState.selectedRow && col == uiState.selectedCol
                        val isValidMove = uiState.validMoves.contains(row * 8 + col)
                        val color = when {
                            isSelected -> selectedColor
                            isValidMove -> validMoveColor
                            else -> baseColor
                        }
                        drawRect(
                            color = color,
                            topLeft = Offset(col * squareSize, row * squareSize),
                            size = Size(squareSize, squareSize)
                        )
                    }
                }
            }

            // Clickable overlays
            for (row in 0..7) {
                for (col in 0..7) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .offset(x = (col * 40).dp, y = (row * 40).dp)
                            .clickable { viewModel.onSquareClick(row, col) }
                    )
                }
            }

            // Pieces
            for (row in 0..7) {
                for (col in 0..7) {
                    val piece = uiState.board.board[row][col] ?: continue
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .offset(x = (col * 40).dp, y = (row * 40).dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(30.dp)) {
                            drawCircle(
                                color = if (piece.color == com.stennu718.myboardgames.feature.checkers.engine.CheckersColor.RED) redColor else blackColor,
                                radius = size.minDimension / 2
                            )
                            if (piece.isKing) {
                                drawCircle(color = Color.Yellow, radius = size.minDimension / 6)
                            }
                        }
                    }
                }
            }
        }
    }
}