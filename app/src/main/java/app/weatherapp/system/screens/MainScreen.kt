package app.weatherapp.system.screens

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.weatherapp.R
import app.weatherapp.presentation.LocationViewModel
import app.weatherapp.system.Routes
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun MainScreen(viewModel: LocationViewModel = koinViewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    val city by viewModel.city.collectAsState()
    val context = LocalContext.current
    val permissionState =
        rememberPermissionState(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    LaunchedEffect(permissionState.status.isGranted) {
        if (permissionState.status.isGranted) {
            viewModel.fetchCity(context)
        } else {
            permissionState.launchPermissionRequest()
        }
    }
    MainScreenContent(
        navController,
        currentRoute,
        searchText = searchText,
        isSearchActive = isSearchActive,
        onSearchTextChange = { searchText = it },
        onSearch = {
            if (searchText.isNotEmpty()) {
                navController.navigate(Routes.MainWeatherScreen.weatherWithCity(searchText))
                isSearchActive = false
                searchText = ""
            } else {
                isSearchActive = false
                return@MainScreenContent
            }
        },
        onSearchToggle = { isSearchActive = !isSearchActive },
        onNavigate = { city -> navController.navigate(Routes.MainWeatherScreen.weatherWithCity(city)) },
        city
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenContent(
    navController: NavHostController,
    currentRoute: String?,
    searchText: String,
    isSearchActive: Boolean,
    onSearchTextChange: (String) -> Unit,
    onSearch: () -> Unit,
    onSearchToggle: () -> Unit,
    onNavigate: (String) -> Unit,
    city: String?
) {
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            if (isSearchActive) {
                TopAppBar(
                    title = {
                        TopAppBarSearchField(
                            searchText,
                            onSearchTextChange
                        )
                    },
                    navigationIcon = { TopAppBarSearchIcon(onSearch) }
                )
            } else {
                TopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = { Text(stringResource(R.string.weather)) },
                    navigationIcon = {
                        IconButton(onClick = onSearchToggle) {
                            Icon(painterResource(R.drawable.baseline_search_24), "search")
                        }
                    },
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                        )
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.Transparent
            ) {
                NavigationBarItem(
                    selected = currentRoute == Routes.MainWeatherScreen.routes,
                    onClick = {
                        onClickNavigation(
                            navController,
                            Routes.MainWeatherScreen.weatherWithCity(city)
                        )
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
                            destination = Routes.SavedWeatherScreen.routes
                        )
                    },
                    icon = {
                        Icon(
                            painterResource(R.drawable.outline_lists_24),
                            contentDescription = stringResource(R.string.saved_cities),
                        )
                    }
                )
            }
        }
    ) { paddingValues ->
        Image(
            painter = painterResource(R.drawable.default_background_one),
            contentDescription = stringResource(R.string.default_background),
            modifier =
                Modifier
                    .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = "weather",
        ) {
            composable(
                route = Routes.MainWeatherScreen.routes,
                arguments =
                    listOf(
                        navArgument("city") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = city
                        }
                    )
            ) { backStackEntry ->
                val cityName = backStackEntry.arguments?.getString("city")
                SelectedWeatherScreen(text = cityName)
            }
            composable(Routes.SavedWeatherScreen.routes) {
                SavedCitiesScreen(
                    onNavigate = onNavigate
                )
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
        placeholder = {
            Text(
                stringResource(R.string.search),
                color = Color.White.copy(alpha = 0.5f)
            )
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors =
            TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
            )
    )
}

@Composable
private fun TopAppBarSearchIcon(onSearch: () -> Unit) {
    IconButton(onClick = onSearch) {
        Icon(painterResource(R.drawable.baseline_done_24), stringResource(R.string.search))
    }
}
