package com.stennu718.myboardgames.feature.puzzles.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stennu718.myboardgames.feature.puzzles.engine.Game2048

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Game2048Screen(onBack: () -> Unit) {
    val game = remember { Game2048() }
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
            Text("2048", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            IconButton(onClick = { game.newGame(); state = game.getState() }) {
                Icon(Icons.Default.Refresh, contentDescription = "New Game")
            }
        }

        Text("Score: ${state.score}", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (state.gameOver) {
            Text("Game Over!", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.error)
        }
        if (state.won) {
            Text("🎉 You win!", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(320.dp)
                .background(Color(0xFFBBADA0))
                .padding(8.dp)
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        val dx = dragAmount.x
                        val dy = dragAmount.y
                        val direction = when {
                            kotlin.math.abs(dx) > kotlin.math.abs(dy) -> {
                                if (dx > 0) Game2048.Direction.RIGHT else Game2048.Direction.LEFT
                            }
                            dy > 0 -> Game2048.Direction.DOWN
                            else -> Game2048.Direction.UP
                        }
                        game.move(direction)
                        state = game.getState()
                    }
                }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                for (r in 0..3) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (c in 0..3) {
                            val value = state.grid[r][c]
                            val bgColor = when (value) {
                                0 -> Color(0xFFCDC1B4)
                                2 -> Color(0xFFEEE4DA)
                                4 -> Color(0xFFEDE0C8)
                                8 -> Color(0xFFF2B179)
                                16 -> Color(0xFFF59563)
                                32 -> Color(0xFFF67C5F)
                                64 -> Color(0xFFF65E3B)
                                128 -> Color(0xFFEDCF72)
                                256 -> Color(0xFFEDCC61)
                                512 -> Color(0xFFEDC850)
                                1024 -> Color(0xFFEDC53F)
                                2048 -> Color(0xFFEDC22E)
                                else -> Color(0xFF3C3A32)
                            }
                            Box(
                                modifier = Modifier.size(72.dp).background(bgColor),
                                contentAlignment = Alignment.Center
                            ) {
                                if (value != 0) {
                                    Text(
                                        value.toString(),
                                        fontSize = if (value > 999) 20.sp else 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (value <= 4) Color(0xFF776E65) else Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Swipe to move tiles", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}