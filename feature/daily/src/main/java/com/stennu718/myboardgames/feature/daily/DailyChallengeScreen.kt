package com.stennu718.myboardgames.feature.daily

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyChallengeScreen(navController: NavController) {
    val challengeManager = remember { DailyChallengeManager() }
    val challenges = remember { challengeManager.getTodayChallenges() }
    val todayDate = remember { challengeManager.getTodayDateString() }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            "Daily Challenge",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            todayDate,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Complete today's challenges to earn XP!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(challenges) { challenge ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        // Navigate to the game
                        when (challenge.gameType) {
                            "chess" -> navController.navigate("chess")
                            "checkers" -> navController.navigate("checkers")
                            "sudoku" -> navController.navigate("sudoku")
                            "blockudoku" -> navController.navigate("blockudoku")
                            else -> navController.navigate("puzzles")
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            when (challenge.gameType) {
                                "chess" -> Icons.Default.Psychology
                                "checkers" -> Icons.Default.GridOn
                                "sudoku" -> Icons.Default.GridView
                                "blockudoku" -> Icons.Default.ViewWeek
                                else -> Icons.Default.Casino
                            },
                            contentDescription = challenge.gameType,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                challenge.gameType.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                challenge.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            "+${challenge.xpReward} XP",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}