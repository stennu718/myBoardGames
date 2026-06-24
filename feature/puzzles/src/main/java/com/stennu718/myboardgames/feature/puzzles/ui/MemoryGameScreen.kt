package com.stennu718.myboardgames.feature.puzzles.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stennu718.myboardgames.feature.puzzles.engine.MemoryGame
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryGameScreen(onBack: () -> Unit) {
    val game = remember { MemoryGame() }
    var state by remember { mutableStateOf(game.newState()) }

    // Auto-flip back unmatched after delay
    LaunchedEffect(state.flippedIndices) {
        if (state.flippedIndices.size == 2 && !state.isComplete) {
            delay(1000)
            game.flipBackUnmatched()
            state = game.getState()
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
            Text("Memory", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            IconButton(onClick = { state = game.newState() }) {
                Icon(Icons.Default.Refresh, contentDescription = "New Game")
            }
        }

        Text("Moves: ${state.moves} | Pairs: ${state.matchedPairs}/8", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isComplete) {
            Text("🎉 Completed in ${state.moves} moves!", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(state.cards) { index, card ->
                val isVisible = card.isFlipped || card.isMatched
                val rotation by animateFloatAsState(if (isVisible) 0f else 180f, label = "rotation")

                Card(
                    modifier = Modifier
                        .size(72.dp)
                        .graphicsLayer { rotationY = rotation },
                    onClick = {
                        game.flipCard(index)
                        state = game.getState()
                    },
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            card.isMatched -> Color(0xFFC8E6C9)
                            card.isFlipped -> Color.White
                            else -> Color(0xFF1565C0)
                        }
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isVisible) {
                            Text(card.emoji, fontSize = 32.sp)
                        } else {
                            Text("?", fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}