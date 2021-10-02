package ru.churkin.cook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import ru.churkin.cook.home.*

class MainActivity : AppCompatActivity() {

    private val viewModel: CookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val state by viewModel.screenState.collectAsState()

            AppTheme {

                Box(modifier = Modifier.fillMaxSize()) {
                    HomeScreen(state, viewModel)

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

