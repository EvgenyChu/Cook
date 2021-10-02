package ru.churkin.cook.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.churkin.cook.domain.Order
import java.util.*

@Composable
fun HomeScreen(state: HomeScreenState, vm: CookViewModel) {

    var isConfirm: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (state.isOpenDialog) {
            CreateOrderDialog(recepts = state.receptsName, vm)
        }

        if (isConfirm) {
            ConfirmDialog()
        }



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
                        OrderCard(order = it, onRemove = { orderId ->
                            vm.removeOrder(orderId)
                        })
                    }
            }
            is OrdersState.ValueWithMessage -> {
                listState.orders
                    .forEach {
                        OrderCard(order = it, onRemove = { orderId ->
                            vm.removeOrder(orderId)
                            isConfirm = true
                        })
                    }
                CircularProgressIndicator(color = MaterialTheme.colors.primary, strokeWidth = 5.dp)
            }
        }
    }
}

@Composable
fun CreateOrderDialog(recepts: List<String>, vm: CookViewModel) {
    Dialog(
        onDismissRequest = {
            vm.toggleDialog()
        })
    {
        Surface(shape = RoundedCornerShape(4.dp)) {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Выберите рецепт",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                recepts.forEach { label ->

                    Row(verticalAlignment = CenterVertically,
                        modifier = Modifier
                            .clickable { vm.addOrder(label) }
                            .height(44.dp)
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = label)
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp)
                ) {
                    TextButton(onClick = { vm.toggleDialog() }) {
                        Text("Отмена", color = MaterialTheme.colors.secondary)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order, modifier: Modifier = Modifier, onRemove: (orderId: Int) -> Unit) {
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
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Text(
                text = order.deadline.format(),
                color = Color.Gray,
//                    fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
            )
            Text(
                text = "${order.price}  руб.",
                color = Color.Black,
//                    fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .background(Color.Yellow, RoundedCornerShape(10.dp))
                    .padding(horizontal = 20.dp, vertical = 5.dp)
            )
            Row() {
                TextButton(onClick = {
                    onRemove(order.id)

                }) {
                    Text(
                        "УДАЛИТЬ",
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {}) {
                    Text(
                        "ПОДРОБНЕЕ",
                    )
                }
            }
        }
    }
}

@Composable
fun ConfirmDialog() {
    AlertDialog(title = {
        Text(text = "Вы точно хотите удалить заказ?")
    },
        buttons = {
            TextButton(onClick = {}) {
                Text(text = "Отмена")

            }
            TextButton(onClick = {}) {
                Text(text = "Удалить")

            }
        },
        onDismissRequest = {})
}

@Preview
@Composable
fun preview() {


    val order = Order(0, "KEKS", Date(), 100, 70, "Anna")

    OrderCard(order = order, onRemove = {})
}

@Preview
@Composable
fun preview1() {


    val order = Order(0, "KEKS", Date(), 100, 70, "Anna")

    ConfirmDialog()
}