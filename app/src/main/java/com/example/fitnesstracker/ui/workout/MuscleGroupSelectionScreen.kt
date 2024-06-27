package com.example.fitnesstracker.ui.workout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesstracker.data.MuscleGroup
import com.example.fitnesstracker.data.muscleGroups

@Composable
fun MuscleGroupSelectionScreen(
    innerPadding: PaddingValues,
    onMuscleGroupClick: (String) -> Unit,
    muscleGroups: List<MuscleGroup>,
) {
    LazyColumn(contentPadding = innerPadding) {
        items(muscleGroups) { lift ->
            Box(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .clickable { onMuscleGroupClick(lift.name) },
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = lift.name, fontSize = 25.sp)
            }
            HorizontalDivider(thickness = 1.dp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMuscleGroupSelectionScreen() {
    MuscleGroupSelectionScreen(
        innerPadding = PaddingValues(),
        onMuscleGroupClick = {},
        muscleGroups = muscleGroups
    )
}