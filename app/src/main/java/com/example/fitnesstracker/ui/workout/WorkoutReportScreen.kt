package com.example.fitnesstracker.ui.workout

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesstracker.data.Exercise
import com.example.fitnesstracker.data.ExerciseSet
import com.example.fitnesstracker.data.WorkoutUIState

@Composable
fun WorkoutReportScreen(
    workoutState: WorkoutUIState,
    innerPadding: PaddingValues,
    viewModel: WorkoutViewModel,
    onAddExerciseClick: () -> Unit
) {
    LazyColumn(
        contentPadding = innerPadding,
    ) {
        item {
            WorkoutHeader(viewModel)
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(thickness = 1.dp)
        }
        itemsIndexed(workoutState.exercises) { i, exercise ->
            ExerciseCard(exercise, i, viewModel)
            HorizontalDivider(thickness = 1.dp)
        }
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = onAddExerciseClick) {
                    Text(text = "Add Exercise")
                }
            }
        }
    }
}

@Composable
fun WorkoutHeader(viewModel: WorkoutViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        NameWeightCard(viewModel)
        Spacer(modifier = Modifier.height(1.dp))
        DateTimeCard(viewModel)
        Spacer(modifier = Modifier.height(1.dp))
        NoteCard(viewModel)
    }
}

@Composable
fun NameWeightCard(viewModel: WorkoutViewModel) {
    val workoutState by viewModel.workoutState.collectAsState()
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.width(4.dp))
        BasicTextFieldOutline(width = 0.675f, contentAlignment = Alignment.BottomCenter) {
            val fontSize = 20.sp
            BasicTextField(
                value = workoutState.name,
                onValueChange = { viewModel.setName(it) },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = fontSize),
                modifier = Modifier.fillMaxWidth(0.95f),
                decorationBox = {
                    GrayTextBackGround("Name", fontSize, workoutState.name.isEmpty())
                    it()
                }
            )
        }
        Spacer(modifier = Modifier.width(1.dp))
        BasicTextFieldOutline(width = 0.95f, contentAlignment = Alignment.BottomCenter) {
            val fontSize = 20.sp
            BasicTextField(
                value = workoutState.bodyWeight,
                onValueChange = { viewModel.setBodyWeight(it) },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = fontSize),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(0.9f),
                decorationBox = {
                    GrayTextBackGround("BW", fontSize, workoutState.bodyWeight.isEmpty())
                    it()
                }
            )
        }
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun DateTimeCard(viewModel: WorkoutViewModel) {
    val workoutState by viewModel.workoutState.collectAsState()
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
    ) {
        val df = SimpleDateFormat("EEE, d MMM")
        val tf = SimpleDateFormat("h:mm a")
        Spacer(Modifier.width(4.dp))
        DateTimeBox(0.35f, df.format(workoutState.date), onClick = { })
        Spacer(Modifier.width(1.dp))
        DateTimeBox(0.5f, tf.format(workoutState.startTime), onClick = { })
        Spacer(Modifier.width(1.dp))
        DateTimeBox(0.95f, text = "End")
    }
}

@Composable
fun DateTimeBox(
    width: Float,
    text: String = "",
    onClick: () -> Unit = {}
) {
    BasicTextFieldOutline(width = width, contentAlignment = Alignment.BottomCenter) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun NoteCard(viewModel: WorkoutViewModel) {
    val workoutState by viewModel.workoutState.collectAsState()
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.width(4.dp))
        BasicTextFieldOutline(width = 0.98f, contentAlignment = Alignment.BottomCenter) {
            val fontSize = 20.sp
            BasicTextField(
                value = workoutState.notes,
                onValueChange = { viewModel.setNotes(it) },
                textStyle = LocalTextStyle.current.copy(fontSize = fontSize),
                modifier = Modifier.fillMaxWidth(0.97f),
                decorationBox = {
                    GrayTextBackGround("Notes", fontSize, workoutState.notes.isEmpty())
                    it()
                }
            )
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    i: Int,
    viewModel: WorkoutViewModel
) {
    Row(modifier = Modifier.padding(top = 2.dp)) {
        Spacer(modifier = Modifier.width(32.dp))
        Text(text = exercise.name, style = MaterialTheme.typography.titleMedium)
    }
    Column {
        exercise.sets.forEachIndexed { j, set ->
            ExerciseContent(
                j,
                weight = set.weight,
                reps = set.reps,
                notes = set.notes,
                onWeightChange = { weight ->
                    val (_, reps, notes) = exercise.sets[j]
                    val sets = exercise.sets.toMutableList()
                    sets[j] = ExerciseSet(weight, reps, notes)
                    viewModel.setExercise(i, exercise.copy(sets = sets))
                },
                onRepsChange = { reps ->
                    val (weight, _, notes) = exercise.sets[j]
                    val sets = exercise.sets.toMutableList()
                    sets[j] = ExerciseSet(weight, reps, notes)
                    viewModel.setExercise(i, exercise.copy(sets = sets))
                },
                onNoteChange = { notes ->
                    val (weight, reps, _) = exercise.sets[j]
                    val sets = exercise.sets.toMutableList()
                    sets[j] = ExerciseSet(weight, reps, notes)
                    viewModel.setExercise(i, exercise.copy(sets = sets))
                },
                onDeleteClick = {
                    val sets = exercise.sets.toMutableList()
                    sets.removeAt(j)
                    viewModel.setExercise(i, exercise.copy(sets = sets))
                }
            )
        }
        Row(horizontalArrangement = Arrangement.Start) {
            Spacer(modifier = Modifier.width(35.dp))
            Box(modifier = Modifier.clickable { viewModel.addExerciseSet(i, ExerciseSet()) }) {
                Text(text = "Add Set", fontSize = 12.sp)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun ExerciseContent(
    i: Int,
    weight: String,
    onWeightChange: (String) -> Unit,
    reps: String,
    onRepsChange: (String) -> Unit,
    notes: String,
    onNoteChange: (String) -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(top = 1.dp)
            .defaultMinSize(minHeight = 60.dp),
    ) {
        var counterClicked by remember { mutableStateOf(false) }
        Spacer(Modifier.width(4.dp))
        if (!counterClicked) {
            Column(modifier = Modifier.fillMaxHeight()) {
                Spacer(Modifier.height(30.dp))
                CountMarker((i + 1).toString(), onClick = { counterClicked = true })
            }

            Spacer(Modifier.width(4.dp))
            Column(modifier = Modifier.height(60.dp)) {
                Text(text = "Weight", fontSize = 13.sp)
                ExerciseInputFields(weight, onWeightChange)
            }

            Spacer(Modifier.width(4.dp))
            Column(modifier = Modifier.height(60.dp)) {
                Text(text = "Reps", fontSize = 13.sp)
                ExerciseInputFields(reps, onRepsChange)
            }

            Spacer(Modifier.width(4.dp))
            Column(modifier = Modifier.height(60.dp)) {
                Text(text = "Volume", fontSize = 13.sp)
                ExerciseVolumeCard(weight.toFloatOrNull(), reps.toIntOrNull())
            }

            Spacer(Modifier.width(4.dp))
            Column {
                Text(text = "Notes", fontSize = 13.sp)
                ExerciseNoteCard(notes, onNoteChange)
            }
        } else {
            ExerciseDeletionCard(
                onCancelClick = {
                    counterClicked = false
                },
                onDeleteClick = {
                    counterClicked = false
                    onDeleteClick()
                }
            )
        }
    }
}

@Composable
fun ExerciseDeletionCard(onDeleteClick: () -> Unit, onCancelClick: () -> Unit) {
    Column {
        HorizontalDivider(thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.fillMaxWidth(0.1f))
            Button(onClick = { onDeleteClick() }, modifier = Modifier.width(150.dp)) {
                Text(text = "Delete")
            }
            Spacer(Modifier.fillMaxWidth(0.1f))
            Button(onClick = { onCancelClick() }, modifier = Modifier.width(150.dp)) {
                Text(text = "Cancel")
            }
        }
        HorizontalDivider(thickness = 1.dp)
    }
}

@Composable
fun ExerciseVolumeCard(weight: Float?, reps: Int?) {
    val volume = if (weight != null && reps != null) (reps * weight).toInt().toString() else ""
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .height(40.dp)
            .width(50.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(volume, style = MaterialTheme.typography.bodyMedium, fontSize = 10.sp)
        }
    }
}

@Composable
fun ExerciseNoteCard(
    notes: String,
    onNoteChange: (String) -> Unit,
) {
    Box(modifier = Modifier.defaultMinSize(minHeight = 40.dp)) {
        BasicTextFieldOutline(width = 0.9f, contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier.defaultMinSize(minHeight = 35.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = notes,
                    onValueChange = onNoteChange,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                )
            }
        }
    }
}


@Composable
fun ExerciseInputFields(value: String, change: (String) -> Unit) {
    Box(
        modifier = Modifier
            .border(
                1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small
            )
            .height(40.dp),
        contentAlignment = Alignment.Center,
    ) {
        BasicTextField(
            value = value,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChange = change,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = 20.sp),
            modifier = Modifier
                .width(50.dp)
        )
    }
}

@Composable
fun CountMarker(count: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(25.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(count, fontSize = 9.sp)
    }
}

@Composable
fun GrayTextBackGround(text: String, fontSize: TextUnit, hide: Boolean) {
    if (hide) {
        Text(
            text = text,
            color = Color.Gray,
            style = TextStyle(fontSize = fontSize),
        )
    }
}

@Composable
fun BasicTextFieldOutline(
    width: Float,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable() (BoxScope.() -> Unit)
) {
    Box(
        modifier = Modifier
            .border(
                1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small
            )
            .fillMaxHeight()
            .fillMaxWidth(width),
        contentAlignment = contentAlignment,
        content = content
    )
}
