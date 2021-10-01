package ru.churkin.cook.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.util.*

@Composable
fun HomeScreen(state: HomeScreenState, vm: CookViewModel) {

    val typography = MaterialTheme.typography
    var isComfirm = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (state.isOpenDialog) {

            CreateOrderDialog(recepts = state.receptsName, vm)
        }

        if(isComfirm.value){
            ConfirmDialog()
        }



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
                        OrderCard(order = it, onRemove = {orderId ->
                            vm.removeOrder(orderId)
                            isComfirm.value = true
                        })
                    }
            }
            is OrdersState.ValueWithMessage -> {
                listState.orders
                    .forEach {
                        OrderCard(order = it,  onRemove = {orderId ->
                            vm.removeOrder(orderId)
                            isComfirm.value = true
                        })
                    }
                CircularProgressIndicator(color = MaterialTheme.colors.primary, strokeWidth = 5.dp)
            }
        }
    }
}

@Composable
fun CreateOrderDialog(recepts: List<String>, vm:CookViewModel) {
    Dialog(
        onDismissRequest = {
            vm.tugleDialog()
        })
    {
        Surface(shape = RoundedCornerShape(10.dp)) {
            Column(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .fillMaxWidth()
            ) {
                recepts.forEach { label ->
                    Row(verticalAlignment = CenterVertically,
                        modifier = Modifier
//                                    .clickable { Log.e("HomeScreen", "$label")}
                            .height(44.dp)

                            .fillMaxWidth()
//                                .clickable { /**/ }
                    ) {
                        TextButton(onClick = { vm.addOrder(label) }) {

                            Text(text = label)
                        }

                    }
                }
                Button(onClick = {
                    vm.tugleDialog()
                }) {
                    Text("Отмена")
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order, modifier: Modifier = Modifier, onRemove : (orderId : Int)->Unit) {
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
                TextButton(onClick = {
                    onRemove(order.id)

                }) {
                    Text(
                        "УДАЛИТЬ",
//                        textDecoration = TextDecoration.Underline
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {}) {
                    Text(
                        "ПОДРОБНЕЕ",
//                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}

@Composable
fun ConfirmDialog (){
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