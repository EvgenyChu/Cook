package ru.churkin.cook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.churkin.cook.data.indigrients.IngridientsScreen
import ru.churkin.cook.home.*
import ru.churkin.cook.recepts.ReceptsScreen

val screens = listOf(
    Screen.Home,
    Screen.Recepts,
    Screen.Ingridients
)

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            AppTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    navController.currentDestination?.label?.toString() ?: "Appbar",
                                    color = MaterialTheme.colors.onPrimary
                                )
                            },
                        )
                    },
                    bottomBar = {
                        BottomNavigation {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            screens.forEach { screen ->
                                BottomNavigationItem(
                                    icon = {
                                        Icon(
                                            painterResource(id = screen.icon),
                                            contentDescription = null
                                        )
                                    },
                                    label = {
                                        Text(
                                            screen.title,
                                            color = MaterialTheme.colors.onPrimary
                                        )
                                    },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        navController.navigate(screen.route)
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = Screen.Home.route,
                        Modifier.padding(innerPadding)
                    ) {
                        composable(
                            Screen.Home.route,
                            arguments = listOf(navArgument("title") {
                                defaultValue = Screen.Home.title
                            })
                        ) { HomeScreen(navController) }
                        composable(Screen.Recepts.route) { ReceptsScreen(navController) }
                        composable(Screen.Ingridients.route) { IngridientsScreen(navController) }
                    }
                }

            }
        }
    }


}

sealed class Screen(val route: String, @DrawableRes val icon: Int, val title: String) {
    object Home : Screen("home", R.drawable.ic_baseline_cake_24, "Заказы")
    object Recepts : Screen("recepts", R.drawable.ic_baseline_construction_24, "Рецепты")
    object Ingridients : Screen("ingridients", R.drawable.ic_baseline_add_24, "Ингредиенты")
}


@Preview
@Composable
fun preview_Homescreen() {
//    HomeScreen(
//        title = "test2222",
//        ordersState = OrdersState.Value(
//            listOf(
//                Order(id = 0, dish = "test", deadline = Date(), 100, 130, "aAnna"),
//                Order(id = 0, dish = "test3", deadline = Date(), 100, 130, "aAnna"),
//            )
//        )
//    )
}

