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

data class DailyChallenge(
    val name: String,
    val description: String,
    val isCompleted: Boolean,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyChallengeScreen(navController: NavController) {
    val challenges = remember {
        listOf(
            DailyChallenge("Chess Puzzle", "Solve 3 chess puzzles", false, Icons.Default.Psychology),
            DailyChallenge("Sudoku", "Complete a Medium Sudoku", false, Icons.Default.GridView),
            DailyChallenge("Checkers", "Win 2 Checkers games", false, Icons.Default.GridOn),
            DailyChallenge("Blockudoku", "Score 100+ points", false, Icons.Default.ViewWeek),
            DailyChallenge("Memory", "Match 10 pairs", false, Icons.Default.Memory)
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            "Daily Challenge",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
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
                    onClick = { /* Navigate to game */ }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            challenge.icon,
                            contentDescription = challenge.name,
                            modifier = Modifier.size(40.dp),
                            tint = if (challenge.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                challenge.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                challenge.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (challenge.isCompleted) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Completed",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}