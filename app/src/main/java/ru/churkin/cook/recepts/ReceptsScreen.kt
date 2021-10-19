package ru.churkin.cook.recepts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.churkin.cook.home.HomeViewModel
import ru.churkin.cook.home.HomeScreenState


@Composable
fun ReceptsScreen(navController: NavController, vm: ReceptsViewModel = viewModel()) {
    val state by vm.screenState.collectAsState()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(state.title)


        Button(onClick = { navController.navigate("home") }) {
            Text("К заказам", color = MaterialTheme.colors.onPrimary)
        }
    }


}