package ru.churkin.cook.home

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.churkin.cook.R
import ru.churkin.cook.domain.Order
import java.util.*

@Composable
fun HomeScreen(navController:NavController,  vm: HomeViewModel = viewModel()) {
    val state by vm.screenState.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (state.isOpenDialog) {
                CreateOrderDialog(recepts = state.receptsName, vm)
            }

            if (state.isConfirm) {
                ConfirmDialog(
                    onDismiss = {
                        vm.warningDialog()
                    },
                    onConfirmRemove = {
                        vm.removeOrder()
                    }
                )
            }

            when (val listState = state.ordersState) {
                is OrdersState.Empty -> {
                    Text("Добавьте заказ", style = MaterialTheme.typography.h5)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.cake_start),
                            contentDescription = null,
                            Modifier.clip(RoundedCornerShape(20))
                        )
                    }
                }
                is OrdersState.Loading -> {
                    Text("Loading...", style = MaterialTheme.typography.body1)
                }
                is OrdersState.Value -> {
                    Log.e("UI", "${listState.orders}")
                    listState.orders
                        .forEach {
                            OrderCard(order = it, onRemove = { orderId ->
                                vm.showRemoveDialog(orderId)
                            })
                        }
                }
                is OrdersState.ValueWithMessage -> {
                    listState.orders
                        .forEach {
                            OrderCard(order = it, onRemove = { orderId ->
                                vm.showRemoveDialog(orderId)
                            })
                        }
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary,
                        strokeWidth = 5.dp
                    )
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomCenter),

            onClick = {
//                navController.popBackStack()
                vm.toggleDialog()
            }) {
            Icon(
                contentDescription = null,
                painter = painterResource(id = R.drawable.ic_baseline_cake_24),
                tint = Color.White
            )
        }
    }
}

@Composable
fun CreateOrderDialog(recepts: List<String>, vm: HomeViewModel) {

    var selectedDishes: List<String> by remember { mutableStateOf(listOf()) }
    var customer by remember { mutableStateOf("") }
    var sliderValue by remember { mutableStateOf(0f) }

    Dialog(
        onDismissRequest = {
            vm.toggleDialog()
        })
    {
        Surface(
            shape = RoundedCornerShape(4.dp),
        )
        {
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
                Column(
                    modifier = Modifier
                        .height(220.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    recepts.forEach { label ->
                        val backgroundColor by animateColorAsState(
                            if (selectedDishes.contains(label)) Color.Red
                            else Color.Transparent
                        )
                        Row(
                            verticalAlignment = CenterVertically,
                            modifier = Modifier
                                .clickable(onClick = {
                                    if (selectedDishes.contains(label)) {
//                                    val newList =  selectedDishes.toMutableList()
//                                        newList.remove(label)
//                                       selectedDishes = newList.toList()


                                        selectedDishes
                                            .toMutableList()
                                            .also {
                                                it.remove(label)
                                                selectedDishes = it
                                            }
                                    } else {
                                        selectedDishes
                                            .toMutableList()
                                            .also {
                                                it.add(label)
                                                selectedDishes = it
                                            }
                                    }
                                    Log.e("HomeScreen", "$selectedDishes $label")
                                })
                                .background(color = backgroundColor)
                                .height(44.dp)
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(text = label)
                        }
                    }
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {}

                Column(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = customer,
                        { customer = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(color = Color.Transparent),
                        textStyle = MaterialTheme.typography.h6,
                        placeholder = { Text("ФИО заказчика") }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {}

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Slider(
                        value = sliderValue,
                        valueRange = 0f..7f,
                        steps = 6,
                        modifier = Modifier.padding(8.dp),
                        onValueChange = { sliderValue = it })
                    Text("Дней до сдачи заказа ${sliderValue.toInt()}")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    TextButton(onClick = { vm.toggleDialog() }) {
                        Text("Отмена", color = MaterialTheme.colors.secondary)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = {
                            vm.addOrder(
                                selectedDishes,
                                customer = customer,
                                deadLineOffset = sliderValue
                            )
                        },
                        enabled = selectedDishes.isNotEmpty()
                    )
                    {
                        Text("Добавить", color = MaterialTheme.colors.secondary)
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
            modifier = Modifier
                .padding(all = 10.dp)
        ) {

            Text(
                text = order.dishes.toString(),
                color = Color.Blue,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            order.customer?.let {
                Text(
                    text = it,
                    color = Color.Gray,
                    //                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                )
            }

            Text(
                text = ("DeadLine ${order.deadline.format()}"),
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
fun ConfirmDialog(onDismiss: () -> Unit, onConfirmRemove: () -> Unit) {
    AlertDialog(
        title = {
            Text(text = "Вы точно хотите удалить заказ?")
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmRemove()
            }) {
                Text(text = "Удалить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Отмена")
            }
        },
        onDismissRequest = onDismiss
    )
}

@Preview
@Composable
fun preview() {


    val order = Order(0, listOf("KEKS", "Cake"), Date(), 100, 70, "Anna")

    OrderCard(order = order, onRemove = {})
}

@Preview
@Composable
fun preview1() {


    val order = Order(0, listOf("KEKS", "Cake"), Date(), 100, 70, "Anna")

    ConfirmDialog({}, {})
}