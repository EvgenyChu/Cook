package ru.churkin.cook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.churkin.cook.home.*
import ru.churkin.cook.recepts.ReceptsScreen
import ru.churkin.cook.recepts.ReceptsScreenState

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val navController = rememberNavController()
            AppTheme {

                Box(modifier = Modifier.fillMaxSize()) {
//                    HomeScreen(state)

                    NavHost(navController = navController, startDestination = "recepts") {
                        composable("home") { HomeScreen(navController) }
                        composable("recepts") { ReceptsScreen(navController) }
                    }

                }
            }
        }
    }


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

