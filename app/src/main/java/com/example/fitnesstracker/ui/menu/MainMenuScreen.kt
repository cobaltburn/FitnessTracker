package com.example.fitnesstracker.ui.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainMenuScreen(
    innerPadding: PaddingValues,
    viewModel: MainMenuViewModel,
    onNewWorkoutClick: () -> Unit
) {
    val menuState by viewModel.menuState.collectAsState()
    LazyColumn(contentPadding = innerPadding) {
        items(menuState.workoutHistory) {

        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        SmallActionButton(onClick = onNewWorkoutClick)
    }
}

@Composable
fun SmallActionButton(onClick: () -> Unit) {
    SmallFloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary,
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "nothing")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainMenuScreen() {
    MainMenuScreen(
        innerPadding = PaddingValues(10.dp),
        viewModel = viewModel(),
        onNewWorkoutClick = {})
}