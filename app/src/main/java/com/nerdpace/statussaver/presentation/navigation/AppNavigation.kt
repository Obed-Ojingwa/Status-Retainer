package com.nerdpace.statussaver.presentation.navigation

// Navigation Setup




import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nerdpace.statussaver.presentation.ui.screen.MainScreen
import com.nerdpace.statussaver.presentation.ui.screen.PreviewScreen
import com.nerdpace.statussaver.presentation.viewmodel.MainViewModel
import com.nerdpace.statussaver.presentation.viewmodel.PreviewViewModel

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nerdpace.statussaver.presentation.ui.screen.MainScreen

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Preview : Screen("preview/{mediaId}") {
        fun createRoute(mediaId: String) = "preview/$mediaId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            val viewModel: MainViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            MainScreen(
                uiState = uiState,
                onEvent = { /* handle events */ },
                onSourceChanged = viewModel::onSourceChanged,
                onTabChanged = viewModel::onTabChanged,
                onScanClick = viewModel::scanStatuses,
                onRequestSafAccess = viewModel::requestSafAccess,
                onSafAccessGranted = viewModel::onSafAccessGranted,
                onMediaClick = { mediaId ->
                    navController.navigate(Screen.Preview.createRoute(mediaId))
                }
            )
        }

        composable(
            route = Screen.Preview.route,
            arguments = listOf(
                navArgument("mediaId") { type = NavType.StringType }
            )
        ) {
            val viewModel: PreviewViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            PreviewScreen(
                media = uiState.media,
                isSaving = uiState.isSaving,
                onSaveClick = viewModel::saveToDevice,
                onShareClick = viewModel::shareMedia,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}