package com.stennu718.myboardgames.feature.sudoku

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.stennu718.myboardgames.feature.sudoku.engine.Difficulty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SudokuScreen(
    navController: NavController,
    viewModel: SudokuViewModel = hiltViewModel()
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
            Text("Sudoku", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Row {
                IconButton(onClick = { viewModel.getHint() }) {
                    Icon(Icons.Default.Lightbulb, contentDescription = "Hint")
                }
                IconButton(onClick = { viewModel.newGame() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "New Game")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.isComplete) {
            Text(
                "🎉 Congratulations! Puzzle solved!",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Difficulty chips
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Difficulty.entries.forEach { diff ->
                FilterChip(
                    selected = uiState.difficulty == diff,
                    onClick = { viewModel.setDifficulty(diff) },
                    label = { Text(diff.name) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isGenerating) {
            CircularProgressIndicator()
        } else {
            uiState.puzzle?.let { puzzle ->
                SudokuGrid(
                    puzzle = puzzle,
                    selectedRow = uiState.selectedRow,
                    selectedCol = uiState.selectedCol,
                    onCellClick = { r, c -> viewModel.selectCell(r, c) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Number pad
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            (1..9).forEach { num ->
                OutlinedButton(
                    onClick = { viewModel.enterNumber(num) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Text(num.toString(), fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = { viewModel.clearCell() }) {
            Text("Clear")
        }
    }
}

@Composable
fun SudokuGrid(
    puzzle: com.stennu718.myboardgames.feature.sudoku.engine.SudokuPuzzle,
    selectedRow: Int?,
    selectedCol: Int?,
    onCellClick: (Int, Int) -> Unit
) {
    val preFilled = Array(9) { BooleanArray(9) }
    for (r in 0..8) for (c in 0..8) {
        preFilled[r][c] = puzzle.solution[r][c] != 0 && puzzle.grid[r][c] == puzzle.solution[r][c] && puzzle.grid[r][c] != 0
    }

    Column(
        modifier = Modifier.border(2.dp, Color.Black)
    ) {
        for (row in 0..8) {
            Row {
                for (col in 0..8) {
                    val value = puzzle.grid[row][col]
                    val isPreFilled = preFilled[row][col]
                    val isSelected = row == selectedRow && col == selectedCol

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .border(0.5.dp, Color.Gray)
                            .border(
                                width = if (row % 3 == 0 || col % 3 == 0) 2.dp else 0.dp,
                                color = Color.Black
                            )
                            .background(
                                when {
                                    isSelected -> Color(0xFFBBDEFB)
                                    else -> Color.White
                                }
                            )
                            .clickable { onCellClick(row, col) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (value != 0) {
                            Text(
                                text = value.toString(),
                                fontSize = 16.sp,
                                fontWeight = if (isPreFilled) FontWeight.Bold else FontWeight.Normal,
                                color = if (isPreFilled) Color.Black else Color.Blue
                            )
                        }
                    }
                }
            }
        }
    }
}