package com.weighttracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.weighttracker.data.database.WeightDatabase
import com.weighttracker.data.repository.WeightRepository
import com.weighttracker.ui.navigation.NavGraph
import com.weighttracker.ui.navigation.Screen
import com.weighttracker.ui.theme.WeightTrackerTheme
import com.weighttracker.viewmodel.WeightViewModel
import com.weighttracker.viewmodel.WeightViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: WeightViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database and repository
        val database = WeightDatabase.getDatabase(applicationContext)
        val repository = WeightRepository(
            weightEntryDao = database.weightEntryDao(),
            goalDao = database.goalDao(),
            userProfileDao = database.userProfileDao()
        )

        // Initialize ViewModel
        val factory = WeightViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[WeightViewModel::class.java]

        setContent {
            WeightTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: WeightViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Define bottom navigation items
    val bottomNavItems = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Default.Home),
        BottomNavItem("Charts", Screen.Charts.route, Icons.Default.ShowChart),
        BottomNavItem("Goals", Screen.Goals.route, Icons.Default.Flag),
        BottomNavItem("Profile", Screen.Profile.route, Icons.Default.Person)
    )

    Scaffold(
        bottomBar = {
            // Only show bottom bar on main screens, not on AddWeight screen
            if (currentDestination?.route in bottomNavItems.map { it.route }) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            viewModel = viewModel
        )
    }
}

data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)
