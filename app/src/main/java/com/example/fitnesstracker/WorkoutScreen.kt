package com.example.fitnesstracker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fitnesstracker.WorkoutScreen.ExerciseSelection
import com.example.fitnesstracker.WorkoutScreen.Main
import com.example.fitnesstracker.WorkoutScreen.MuscleGroupSelection
import com.example.fitnesstracker.data.Exercise
import com.example.fitnesstracker.data.muscleGroupMap
import com.example.fitnesstracker.data.muscleGroups
import com.example.fitnesstracker.ui.menu.MainMenuScreen
import com.example.fitnesstracker.ui.menu.MainMenuViewModel
import com.example.fitnesstracker.ui.workout.ExerciseSelectionScreen
import com.example.fitnesstracker.ui.workout.MuscleGroupSelectionScreen
import com.example.fitnesstracker.ui.workout.WorkoutReportScreen
import com.example.fitnesstracker.ui.workout.WorkoutViewModel

interface Screen {
    val route: String
    fun title(): String
    fun fillWidth(): Float
}

sealed class WorkoutScreen(override val route: String) : Screen {
    data object Main : WorkoutScreen("WorkoutScreen") {
        override fun title(): String = "Workout"
        override fun fillWidth(): Float = 0.85f
    }

    data object MuscleGroupSelection : WorkoutScreen("MuscleGroupSelection") {
        override fun title(): String = "Muscle Group"
        override fun fillWidth(): Float = 0.85f
    }

    data object ExerciseSelection : WorkoutScreen("ExerciseSelection") {
        override fun title(): String = "Select"
        override fun fillWidth(): Float = 0.87f
    }

}

sealed class MenuScreen(override val route: String) : Screen {
    data object Main : MenuScreen("MenuScreen") {
        override fun title(): String = "Workouts"
        override fun fillWidth(): Float = 0.95f
    }
}

fun currentScreen(route: String): Screen = when (route) {
    "WorkoutScreen" -> Main
    "MuscleGroupSelection" -> MuscleGroupSelection
    "ExerciseSelection/{muscleGroup}" -> ExerciseSelection
    else -> MenuScreen.Main
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutTopBar(
    currentScreen: Screen,
    navigateUp: () -> Unit,
    canNavigateBack: Boolean,
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(currentScreen.fillWidth()),
                contentAlignment = Alignment.Center,
                content = { Text(text = currentScreen.title(), textAlign = TextAlign.Center) }
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

@Composable
fun FitnessTrackerApp(
    viewModel: WorkoutViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = currentScreen(
        backStackEntry?.destination?.route ?: MenuScreen.Main.route
    )



    Scaffold(
        modifier = Modifier.fillMaxHeight(),
        topBar = {
            WorkoutTopBar(
                currentScreen = currentScreen,
                navigateUp = { navController.navigateUp() },
                canNavigateBack = navController.previousBackStackEntry != null
            )
        }
    ) { innerPadding ->
        val workoutUIState by viewModel.workoutState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = MenuScreen.Main.route
        ) {
            composable(route = Main.route) {
                WorkoutReportScreen(
                    workoutState = workoutUIState,
                    innerPadding = innerPadding,
                    viewModel = viewModel,
                    onAddExerciseClick = {
                        navController.navigate(MuscleGroupSelection.route)
                    }
                )
            }

            composable(
                "${ExerciseSelection.route}/{muscleGroup}"
            ) { backStackEntry ->
                val muscleGroup = backStackEntry.arguments?.getString("muscleGroup")
                println(muscleGroup)
                ExerciseSelectionScreen(
                    innerPadding = innerPadding,
                    onAddExerciseClick = {
                        viewModel.addExercise(Exercise(name = it))
                        navController.popBackStack(Main.route, inclusive = false)
                    },
                    exercises = muscleGroupMap[muscleGroup]!!
                )
            }

            composable(route = MuscleGroupSelection.route) {
                MuscleGroupSelectionScreen(
                    innerPadding = innerPadding,
                    onMuscleGroupClick = { muscleGroup ->
                        val selectMuscle =
                            "${ExerciseSelection.route}/$muscleGroup"
                        navController.navigate(selectMuscle)
                    },
                    muscleGroups = muscleGroups
                )
            }

            composable(route = MenuScreen.Main.route) {
                MainMenuScreen(
                    innerPadding = innerPadding,
                    viewModel = MainMenuViewModel(),
                    onNewWorkoutClick = { navController.navigate(Main.route) },
                    onNewCameraClick = {}
                )
            }
        }
    }
}