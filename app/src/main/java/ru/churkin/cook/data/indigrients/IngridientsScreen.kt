package ru.churkin.cook.data.indigrients

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.churkin.cook.R
import ru.churkin.cook.recepts.CreateReceptDialog
import ru.churkin.cook.recepts.ReceptCard

@Composable
fun IngridientsScreen(navController: NavController, vm: IngridientsViewModel = viewModel()) {
    val state by vm.screenState.collectAsState()

/*    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(state.title)

            if (state.isCreateDialog) {
                CreateReceptDialog(onDismiss = {vm.hideCreateDialog()}, onCreate = {title, costP  ->vm.addIngridient(title, costP)})
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
                is IngridientsState.Loading -> {
                    Text("Loading...", style = MaterialTheme.typography.body1)
                }
                is IngridientsState.Value -> {
                    listState.ingridients
                        .forEach {
                            ReceptCard(recept = it, onRemove = { orderId ->
                                vm.showRemoveDialog(orderId)
                            })
                        }
                }
                is IngridientsState.ValueWithMessage -> {
                    listState.indigrients
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
} */
}