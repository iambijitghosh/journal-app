package com.journal.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.journal.app.ui.calendar.CalendarScreen
import com.journal.app.ui.editor.EditorScreen

sealed class Screen(val route: String) {
    object Calendar : Screen("calendar")
    object Editor : Screen("editor/{date}") {
        fun createRoute(date: String) = "editor/$date"
    }
}

@Composable
fun JournalNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Calendar.route,
        modifier = modifier
    ) {
        composable(Screen.Calendar.route) {
            CalendarScreen(
                onDateClick = { date ->
                    navController.navigate(Screen.Editor.createRoute(date))
                },
                onTodayClick = {
                    val today = java.time.LocalDate.now().toString()
                    navController.navigate(Screen.Editor.createRoute(today))
                }
            )
        }

        composable(
            route = Screen.Editor.route,
            arguments = listOf(navArgument("date") { type = NavType.StringType })
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: return@composable
            EditorScreen(
                date = date,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
