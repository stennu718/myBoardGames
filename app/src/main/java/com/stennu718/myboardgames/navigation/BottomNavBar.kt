package com.stennu718.myboardgames.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.ViewWeek
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Chess, "Chess", Icons.Default.Psychology),
    BottomNavItem(Screen.PuzzleRush, "Rush", Icons.Default.Speed),
    BottomNavItem(Screen.Stats, "Stats", Icons.Default.Analytics),
    BottomNavItem(Screen.Daily, "Daily", Icons.Default.Today),
    BottomNavItem(Screen.Profile, "Profile", Icons.Default.Person),
)

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.screen.route,
                onClick = { onNavigate(item.screen.route) }
            )
        }
    }
}