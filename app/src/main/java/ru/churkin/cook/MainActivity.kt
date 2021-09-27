package ru.churkin.cook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import java.util.*

class MainActivity : AppCompatActivity() {

    private val viewModel: CookViewModel by viewModels()
    private val title: String = "any title"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state by viewModel.screenState.collectAsState()

            Log.e("STATE", "$state")
            Box(modifier = Modifier.fillMaxSize()) {
                HomeScreen(state)
                FloatingActionButton(
                    modifier = Modifier.align(Alignment.BottomCenter),

                    onClick = { viewModel.tugleDialog() }) {
                    Icon(
                        contentDescription = null,
                        painter = painterResource(id = R.drawable.ic_baseline_cake_24),
                        tint = Color.White
                    )
                }
            }


            /*Log.e("Contenr", "${state.orders}")
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Column {


                    //orders.forEach{
                    //  OrderCard(order = it)
                    // }
                }


            }

            /* Box(
                 contentAlignment = Alignment.Center,
                 modifier = Modifier
                     .background(Color.Blue)
                     .fillMaxWidth()
                     .aspectRatio(16 / 9f)

             ) {

                 Text(text = "any long text", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                 Text(
                     text = "other long text",
                     modifier = Modifier
                         .offset(y = 20.dp)
                         .background(Color.Cyan),
                     color = Color.Red,
                     fontSize = 16.sp,
                     fontWeight = FontWeight.Bold
                 )
             }*/

            /* Row(
                 verticalAlignment = Alignment.Bottom,
                 modifier = Modifier.fillMaxWidth()
             ) {
                 Box(
                     modifier = Modifier
                         .size(100.dp)
                         .background(color = Color.Red)
                 )
                 Box(
                     modifier = Modifier
                         .size(200.dp)
                         .background(color = Color.Green)
                 )
                 Box(
                     modifier = Modifier
                         .size(50.dp)
                         .background(color = Color.DarkGray)
                 )
             }*/

            /*Card(modifier = Modifier.padding(16.dp)) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.72f)
                            .background(Color.Blue)
                    )
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "something title", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { }) {
                            Text(text = "add to cart")
                        }
                    }
                }
            }*/


        }*/

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

