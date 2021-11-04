package ru.churkin.cook.recepts

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.churkin.cook.R

@Composable
fun ReceptCard(recept: Recept, modifier: Modifier = Modifier, onRemove: (receptId: Int) -> Unit) {
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
                text = recept.dish,
                color = Color.Blue,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            Text(
                text = "${recept.costPrice}  руб.",
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
                    onRemove(recept.id)

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
fun ReceptsScreen(navController: NavController, vm: ReceptsViewModel = viewModel()) {
    val state by vm.screenState.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(state.title)

            if (state.isCreateDialog) {
                CreateReceptDialog(onDismiss = {vm.hideCreateDialog()}, onCreate = {dish, ing ->vm.addRecept(dish, ing)})
            }

            /*if (state.isConfirm) {
                ConfirmReceptDialog(
                    onDismiss = {
                        vm.warningDialog()
                    },
                    onConfirmRemove = {
                        vm.removeRecept()
                    }
                )
            }*/

            when (val listState = state.receptsState) {
                is ReceptsState.Empty -> {
                    Text("Добавьте рецепт", style = MaterialTheme.typography.h5)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_baseline_add_24),
                            contentDescription = null,
                            Modifier.clip(RoundedCornerShape(20))
                        )
                    }
                }
                is ReceptsState.Loading -> {
                    Text("Loading...", style = MaterialTheme.typography.body1)
                }
                is ReceptsState.Value -> {
                    listState.recepts
                        .forEach {
                            ReceptCard(recept = it, onRemove = { orderId ->
                                vm.showRemoveDialog(orderId)
                            })
                        }
                }
                is ReceptsState.ValueWithMessage -> {
                    listState.recepts
                        .forEach {
                            ReceptCard(recept = it, onRemove = { receptId ->
                                vm.showRemoveDialog(receptId)
                            })
                        }
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = {
                vm.showCreateDialog()
            }) {
            Icon(
                contentDescription = null,
                painter = painterResource(id = R.drawable.ic_baseline_construction_24),
                tint = Color.White
            )
        }
    }
}

@Composable
fun CreateReceptDialog(
    onDismiss: () -> Unit,
    onCreate: (addDish: String, addIndigrients: String) -> Unit
) {
    var addDish by remember { mutableStateOf("") }
    var addIndigrients by remember { mutableStateOf("") }
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(4.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {

                Text(
                    text = "Добавьте рецепт",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = addDish,
                        { addDish = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(color = Color.Transparent),
                        textStyle = MaterialTheme.typography.h6,
                        placeholder = { Text("Наименование рецепта") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = addIndigrients,
                        { addIndigrients = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(color = Color.Transparent),
                        textStyle = MaterialTheme.typography.h6,
                        placeholder = { Text("Наименование ингредиентов") }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    TextButton(onClick = onDismiss) {
                        Text("Отмена", color = MaterialTheme.colors.secondary)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = {
                            onCreate(
                                addDish,
                                addIndigrients
                            )
                        },
                        enabled = addDish.isNotEmpty()
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
fun ConfirmReceptDialog(onDismiss: () -> Unit, onConfirmRemove: () -> Unit) {
    AlertDialog(
        title = {
            Text(text = "Вы точно хотите удалить рецепт?")
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


//    @Composable
//    fun CreateReceptDialog(vm: ReceptsViewModel) {
//    }