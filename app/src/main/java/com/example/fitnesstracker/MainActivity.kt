package com.example.fitnesstracker

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import com.example.fitnesstracker.ui.theme.FitnessTrackerTheme
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkoutPage()
        }
    }
}

enum class WorkoutScreen {
    ExerciseSection,
    Main
}

@Composable
fun WorkoutPage() {
    FitnessTrackerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Scaffold(
                modifier = Modifier.fillMaxHeight(),
                topBar = {
                    WorkoutTopBar()
                }
            ) { innerPadding ->
                WorkoutScreen(innerPadding)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutTopBar() {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Date", textAlign = TextAlign.Center)
            }
        }
    )
}

@Composable
fun WorkoutScreen(innerPadding: PaddingValues) {
    val lifts = remember { mutableStateListOf<String>("Bench Press", "Squat") }
    LazyColumn(
        contentPadding = innerPadding,
    ) {
        item {
            WorkoutHeader()
        }
        items(lifts) { lift ->
            Exercise(lift)
        }
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { lifts.add("Dead Lift") }) {
                    Text(text = "Add Exercise")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutHeader() {
    Column(modifier = Modifier.fillMaxWidth()) {
        NameWeightCard()
        Spacer(modifier = Modifier.height(1.dp))
        DateTimeCard()
        Spacer(modifier = Modifier.height(1.dp))
        NoteCard()
    }
}

@Composable
fun NameWeightCard() {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.width(4.dp))
        BasicTextFieldOutline(width = 0.675f, contentAlignment = Alignment.BottomCenter) {
            var name by remember { mutableStateOf(TextFieldValue()) }
            val fontSize = 20.sp
            BasicTextField(
                value = name,
                onValueChange = { name = it },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = fontSize),
                modifier = Modifier.fillMaxWidth(0.95f),
                decorationBox = {
                    GrayTextBackGround("Name", fontSize, name.text.isEmpty())
                    it()
                }
            )
        }
        Spacer(modifier = Modifier.width(1.dp))
        BasicTextFieldOutline(width = 0.95f, contentAlignment = Alignment.BottomCenter) {
            var bodyWeight by remember { mutableStateOf(TextFieldValue()) }
            val fontSize = 20.sp
            BasicTextField(
                value = bodyWeight,
                onValueChange = { bodyWeight = it },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = fontSize),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(0.9f),
                decorationBox = {
                    GrayTextBackGround("BW", fontSize, bodyWeight.text.isEmpty())
                    it()
                }
            )
        }
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun DateTimeCard() {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
    ) {
        val currentDate = Date()
        val df = SimpleDateFormat("EEE, d MMM")
        val tf = SimpleDateFormat("h:mm a")
        var showDatePopup by remember { mutableStateOf(false) }
        var showStartTimePopup by remember { mutableStateOf(false) }
        Spacer(Modifier.width(4.dp))
        PopUpBox(showPopup = showDatePopup, onClickOutside = { showDatePopup = false })
        PopUpBox(showPopup = showStartTimePopup, onClickOutside = { showStartTimePopup = false })
        DateTimeBox(0.35f, df.format(currentDate), onClick = { showDatePopup = true })
        Spacer(Modifier.width(1.dp))
        DateTimeBox(0.5f, tf.format(currentDate), onClick = { showStartTimePopup = true })
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
fun PopUpBox(showPopup: Boolean, onClickOutside: () -> Unit) {
    if (showPopup) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(10f),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Popup(
                    onDismissRequest = { onClickOutside() }
                ) {
                    Text(text = "Here is the pop up box")
                }
            }
        }
    }
}

@Composable
fun NoteCard() {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.width(4.dp))
        BasicTextFieldOutline(width = 0.98f, contentAlignment = Alignment.BottomCenter) {
            var notes by remember { mutableStateOf(TextFieldValue()) }
            val fontSize = 20.sp
            BasicTextField(
                value = notes,
                onValueChange = { notes = it },
                textStyle = LocalTextStyle.current.copy(fontSize = fontSize),
                modifier = Modifier.fillMaxWidth(0.97f),
                decorationBox = {
                    GrayTextBackGround("Notes", fontSize, notes.text.isEmpty())
                    it()
                }
            )
        }
    }
}

@Composable
fun Exercise(name: String) {
    Row(modifier = Modifier.padding(top = 2.dp)) {
        Spacer(modifier = Modifier.width(32.dp))
        Text(text = name, style = MaterialTheme.typography.titleMedium)
    }
    var setCount by remember { mutableIntStateOf(1) }
    Column {
        repeat(setCount) { ExerciseCard((it + 1).toString()) }
        Row(horizontalArrangement = Arrangement.Start) {
            Spacer(modifier = Modifier.width(35.dp))
            Box(modifier = Modifier.clickable { setCount++ }) {
                Text(text = "Add Set", fontSize = 12.sp)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun ExerciseCard(index: String = "") {
    var weight by rememberSaveable { mutableStateOf("") }
    var reps by rememberSaveable { mutableStateOf("") }
    ExerciseContent(index, weight, { weight = it }, reps, { reps = it })
}

@Composable
fun ExerciseContent(
    count: String,
    weight: String,
    onWeightChange: (String) -> Unit,
    reps: String,
    onRepsChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(top = 1.dp)
            .defaultMinSize(minHeight = 60.dp),
    ) {
        Spacer(Modifier.width(4.dp))
        Column(modifier = Modifier.fillMaxHeight()) {
            Spacer(Modifier.height(30.dp))
            CountMarker(count)
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
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .height(40.dp)
                    .width(50.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    val w = weight.toIntOrNull()
                    val r = reps.toIntOrNull()
                    val volume = if (w != null && r != null) (r * w).toString() else ""
                    Text(volume, style = MaterialTheme.typography.bodyMedium, fontSize = 10.sp)
                }
            }
        }
        Spacer(Modifier.width(4.dp))
        Column {
            Text(text = "Notes", fontSize = 13.sp)
            var notes by remember { mutableStateOf("") }
            Box(modifier = Modifier.defaultMinSize(minHeight = 40.dp)) {
                BasicTextFieldOutline(width = 0.9f, contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier.defaultMinSize(minHeight = 35.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        BasicTextField(
                            value = notes,
                            onValueChange = { notes = it },
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
    }
}

@Composable
fun ExerciseNoteCard() {
    Column {
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
fun CountMarker(count: String) {
    Box(
        modifier = Modifier
            .width(25.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ),
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
