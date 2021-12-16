package ru.churkin.cook.data.indigrients

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.churkin.cook.R
import ru.churkin.cook.Screen
import ru.churkin.cook.domain.Ingridient
import ru.churkin.cook.home.format
import ru.churkin.cook.home.parseDate
import ru.churkin.cook.recepts.CreateReceptDialog
import ru.churkin.cook.recepts.ReceptCard
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RowIngridient(
    ingridient: Ingridient,
    modifier: Modifier = Modifier,
    vm: IngridientsViewModel = viewModel()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = ingridient.title,
            color = Color.Blue,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp)
        )

        Text(
            text = "${ingridient.costPrice}  руб.",
            color = Color.Black,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.body2,
            modifier = Modifier
                .background(Color.Yellow, RoundedCornerShape(10.dp))
                .padding(horizontal = 20.dp, vertical = 5.dp)
        )
    }
}

@Composable
fun IngridientsScreen(navController: NavController, vm: IngridientsViewModel = viewModel()) {
    val state by vm.screenState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                state.title, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center
            )

            if (state.isCreateDialog) {
                CreateIngridientDialog(
                    onDismiss = { vm.hideCreateDialog() },
                    onCreate = { title, costP, avaliable, buyAt ->
                        vm.addIngridient(
                            title,
                            costP,
                            avaliable,
                            buyAt
                        )
                    })
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

            when (val listState = state.ingridientsState) {
                is IngridientsState.Empty -> {
                    Text("Добавьте ингридиент", style = MaterialTheme.typography.h5)
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
                is IngridientsState.Loading -> {
                    Text("Loading...", style = MaterialTheme.typography.body1)
                }
                is IngridientsState.Value -> {
                    listState.ingridients
                        .forEach {
                            RowIngridient(ingridient = it)
                        }
                }
                is IngridientsState.ValueWithMessage -> {
                    listState.indigrients
                        .forEach {
                            RowIngridient(ingridient = it)
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
fun CreateIngridientDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, costPrice: Int, avaliable: Int, buyAt: Date) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var costPrice by remember { mutableStateOf(0) }
    var avaliable by remember { mutableStateOf(0) }
    var buyAt by remember { mutableStateOf(Date()) }
    var isShowDatePicker by remember { mutableStateOf(false) }

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
                    text = "Добавьте ингридиент",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = title,
                        { title = it },
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
                        "$costPrice",
                        { costPrice = it.toInt() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(color = Color.Transparent),
                        textStyle = MaterialTheme.typography.h6,
                        placeholder = { Text("Цена ингридиента за грамм") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        "$avaliable",
                        { avaliable = it.toInt() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(color = Color.Transparent),
                        textStyle = MaterialTheme.typography.h6,
                        placeholder = { Text("Количество") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        "${buyAt.format()}",
                        {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(color = Color.Transparent)
                            .clickable { isShowDatePicker = true },
                        enabled = false,
                        textStyle = MaterialTheme.typography.h6,
                        placeholder = { Text("Количество") }
                    )
                    Button(onClick = { isShowDatePicker = true }) {
                        Text("Lfnf")
                    }
                    if (isShowDatePicker) DatePicker(
                        onSelect = {
                            Log.e("IngridientsScreen", "$it")
                            buyAt = it
                            isShowDatePicker = false
                        },
                        onDismiss = { isShowDatePicker = false })
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
                                title,
                                costPrice,
                                avaliable,
                                buyAt
                            )
                        },
                        enabled = title.isNotEmpty()
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
fun DatePicker(onSelect: (Date) -> Unit, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)


    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            Log.e("IngridientsScreen", "Select $year $month $dayOfMonth")
            onSelect("$dayOfMonth.$month.$year".parseDate())
        }, year, month, day
    )
    datePickerDialog.setOnDismissListener {
        onDismiss()
    }
    datePickerDialog.show()

    /*Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Выберети дату поступления продукта: ${date.value}")
        Button(onClick = {
            datePickerDialog.show()
        }) {
            Text(text = "Календарь")
        }
    }*/
}
