package ru.churkin.cook.home

import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun HomeScreen(state: HomeScreenState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MaterialTheme {
            val typography = MaterialTheme.typography
            Text(
                text = state.title,
                modifier = Modifier.fillMaxWidth(),
                style = typography.h5,
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )

            when (val listState = state.ordersState) {
                is OrdersState.Empty -> {
                    Text("Добавьте заказ", style = typography.body1)
                }
                is OrdersState.Loading -> {
                    Text("Loading...", style = typography.body1)
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
}

@Composable
fun OrderCard(order: Order, modifier: Modifier = Modifier) {
    MaterialTheme {
        val typography = MaterialTheme.typography
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
                    style = typography.h5,
                    //                   fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Text(
                    text = "${order.deadline.format()}",
                    color = Color.Gray,
//                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    style = typography.body1,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                )
                Text(
                    text = "${order.price}  руб.",
                    color = Color.Black,
//                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    style = typography.body2,
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
}