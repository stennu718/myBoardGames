package com.stennu718.myboardgames.feature.blockudoku

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockudokuScreen(
    navController: NavController,
    viewModel: BlockudokuViewModel = hiltViewModel()
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
            Text("Blockudoku", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            IconButton(onClick = { viewModel.newGame() }) {
                Icon(Icons.Default.Refresh, contentDescription = "New Game")
            }
        }

        Text("Score: ${uiState.score}", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.isGameOver) {
            Text(
                "Game Over! Score: ${uiState.score}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Board
        BlockudokuBoardGrid(
            board = uiState.board,
            onCellClick = { r, c -> viewModel.placePiece(r, c) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pieces
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            uiState.availablePieces.forEachIndexed { index, piece ->
                val isSelected = index == uiState.selectedPieceIndex
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clickable { viewModel.selectPiece(index) }
                        .background(if (isSelected) Color(0xFFBBDEFB) else Color(0xFFF5F5F5))
                        .border(2.dp, if (isSelected) Color.Blue else Color.Gray)
                        .padding(4.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        piece.forEach { row ->
                            Row {
                                row.forEach { cell ->
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(if (cell) Color(0xFF4CAF50) else Color.Transparent)
                                            .border(0.5.dp, Color.Gray)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BlockudokuBoardGrid(
    board: com.stennu718.myboardgames.feature.blockudoku.engine.BlockudokuBoard,
    onCellClick: (Int, Int) -> Unit
) {
    Column(
        modifier = Modifier.border(2.dp, Color.Black)
    ) {
        for (row in 0..8) {
            Row {
                for (col in 0..8) {
                    val isFilled = board.grid[row][col]
                    val inBox = (row / 3 + col / 3) % 2 == 0
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .border(0.5.dp, Color.Gray)
                            .background(
                                when {
                                    isFilled -> Color(0xFF4CAF50)
                                    inBox -> Color(0xFFF5F5F5)
                                    else -> Color.White
                                }
                            )
                            .clickable { onCellClick(row, col) },
                        contentAlignment = Alignment.Center
                    ) {}
                }
            }
        }
    }
}