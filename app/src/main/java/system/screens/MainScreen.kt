package system.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.weatherapp.R
import system.Routes

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    MainScreenContent(
        navController, currentRoute, searchText = searchText,
        isSearchActive = isSearchActive,
        onSearchTextChange = { searchText = it },
        onSearch = {
            navController.navigate(Routes.MainWeatherScreen.weatherWithCity(searchText))
            isSearchActive = false
            searchText = ""
        },
        onSearchToggle = { isSearchActive = !isSearchActive }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenContent(
    navController: NavHostController,
    currentRoute: String?,
    searchText: String,
    isSearchActive: Boolean,
    onSearchTextChange: (String) -> Unit,
    onSearch: () -> Unit,
    onSearchToggle: () -> Unit


) {

    Scaffold(
        topBar = {
            if (isSearchActive) {
                TopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = {
                        TopAppBarSearchField(
                            searchText, onSearchTextChange
                        )
                    },
                    navigationIcon = { TopAppBarSearchIcon(onSearch) }
                )
            } else {
                TopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = { Text("Weather") },
                    navigationIcon = {
                        IconButton(onClick = onSearchToggle) {
                            Icon(painterResource(R.drawable.baseline_search_24), "search")
                        }
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == Routes.MainWeatherScreen.routes,
                    onClick = {
                        navController.navigate(Routes.MainWeatherScreen.weatherWithCity(null)) {
                            launchSingleTop = true
                            popUpTo(Routes.MainWeatherScreen.routes)
                        }
                    },
                    icon = {
                        Icon(painterResource(R.drawable.outline_cloud_24), "Home")
                    }
                )
                NavigationBarItem(
                    selected = currentRoute == Routes.SavedWeatherScreen.routes,
                    onClick = {
                        onClickNavigation(
                            navController = navController,
                            destination = Routes.SavedWeatherScreen.routes,
                        )
                    },
                    icon = {
                        Icon(
                            painterResource(R.drawable.outline_lists_24),
                            contentDescription = "SavedCities",
                        )
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "weather",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(
                route = Routes.MainWeatherScreen.routes,  // "weather?city={city}"
                arguments = listOf(navArgument("city") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
            ) { backStackEntry ->
                val city = backStackEntry.arguments?.getString("city")
                SelectedWeatherScreen(text = city)
            }
            composable(Routes.SavedWeatherScreen.routes) {  // "saved"
                SavedCitiesScreen()
            }
        }
    }
}

private fun onClickNavigation(
    navController: NavHostController,
    destination: String,
    home: String = Routes.MainWeatherScreen.routes,
) {
    navController.navigate(destination) {
        launchSingleTop = true
        popUpTo(home)
    }
}

@Composable
private fun TopAppBarSearchField(
    searchText: String,
    onSearchTextChange: (String) -> Unit,

    ) {
    TextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        placeholder = { Text("Search city...") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun TopAppBarSearchIcon(
    onSearch: () -> Unit
) {
    IconButton(onClick = onSearch) {
        Icon(painterResource(R.drawable.baseline_done_24), "search")
    }
}

