package ru.churkin.cook.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.text.style.TextDecoration
import ru.churkin.cook.domain.Order

@Composable
fun HomeScreen(state: HomeScreenState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

            Text(
                text = state.title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )

            when (val listState = state.ordersState) {
                is OrdersState.Empty -> {
                    Text("Добавьте заказ", style = MaterialTheme.typography.body1)
                }
                is OrdersState.Loading -> {
                    Text("Loading...", style = MaterialTheme.typography.body1)
                }
                is OrdersState.Value -> {
                    Log.e("UI", "${listState.orders}")
                    listState.orders
                        .forEach {
                            OrderCard(order = it)
                        }
                }
                is OrdersState.ValueWithMessage -> {
                    listState.orders
                        .forEach {
                            OrderCard(order = it)
                        }
                        CircularProgressIndicator(color = MaterialTheme.colors.primary, strokeWidth = 5.dp)
                }
            }
        }
}

@Composable
fun OrderCard(order: Order, modifier: Modifier = Modifier) {
        Card(
            elevation = 4.dp,
            modifier = modifier
                .padding(all = 10.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(all = 10.dp)
            ) {

                Text(
                    text = order.dish,
                    color = Color.Blue,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Text(
                    text = "${order.deadline.format()}",
                    color = Color.Gray,
//                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                )
                Text(
                    text = "${order.price}  руб.",
                    color = Color.Black,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .background(Color.Yellow, RoundedCornerShape(10.dp))
                        .padding(horizontal = 20.dp, vertical = 5.dp)
                )
                Row() {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = { }) {
                        Text("ПОДРОБНЕЕ",
                            textDecoration = TextDecoration.Underline)
                    }
                }
            }
        }
}