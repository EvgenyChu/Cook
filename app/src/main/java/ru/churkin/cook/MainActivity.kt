package ru.churkin.cook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import ru.churkin.cook.domain.Order
import ru.churkin.cook.home.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val viewModel: CookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state by viewModel.screenState.collectAsState()
            AppTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    HomeScreen(state)
                    FloatingActionButton(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        onClick = { viewModel.addOrder() }) {
                        Icon(
                            contentDescription = null,
                            painter = painterResource(id = R.drawable.ic_baseline_cake_24),
                            tint = MaterialTheme.colors.onSecondary
                        )
                    }
                }
            }
        }
    }


}

@Preview
@Composable
fun preview() {


    val order = Order(0, "KEKS", Date(), 100, 70, "Anna")

    OrderCard(order = order)
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

