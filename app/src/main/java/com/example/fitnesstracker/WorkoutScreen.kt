package com.example.fitnesstracker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitnesstracker.data.Exercise
import com.example.fitnesstracker.data.muscleGroupMap
import com.example.fitnesstracker.data.muscleGroups
import com.example.fitnesstracker.ui.menu.MainMenuScreen
import com.example.fitnesstracker.ui.menu.MainMenuViewModel
import com.example.fitnesstracker.ui.theme.FitnessTrackerTheme
import com.example.fitnesstracker.ui.workout.ExerciseSelectionScreen
import com.example.fitnesstracker.ui.workout.MuscleGroupSelectionScreen
import com.example.fitnesstracker.ui.workout.WorkoutReportScreen
import com.example.fitnesstracker.ui.workout.WorkoutViewModel

sealed class WorkoutScreen(val route: String) {
    data object Main : WorkoutScreen("WorkoutScreen")

    data object MuscleGroupSelection : WorkoutScreen("MuscleGroupSelection")

    data object ExerciseSelection : WorkoutScreen("ExerciseSelection")
}

sealed class MenuScreen(val route: String) {
    data object Main : MenuScreen("MenuScreen")
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
fun FitnessTrackerApp(
    viewModel: WorkoutViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    FitnessTrackerTheme {
        Scaffold(
            modifier = Modifier.fillMaxHeight(),
            topBar = {
                WorkoutTopBar()
            }
        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = MenuScreen.Main.route
            ) {
                composable(route = WorkoutScreen.Main.route) {
                    WorkoutReportScreen(
                        innerPadding = innerPadding,
                        viewModel = viewModel,
                        onAddExerciseClick = {
                            navController.navigate(WorkoutScreen.MuscleGroupSelection.route)
                        }
                    )
                }

                composable(
                    "${WorkoutScreen.ExerciseSelection.route}/{muscleGroup}"
                ) { backStackEntry ->
                    val muscleGroup = backStackEntry.arguments?.getString("muscleGroup")
                    println(muscleGroup)
                    ExerciseSelectionScreen(
                        innerPadding = innerPadding,
                        onAddExerciseClick = {
                            viewModel.addExercise(Exercise(name = it))
                            navController.popBackStack(WorkoutScreen.Main.route, inclusive = false)
                        },
                        exercises = muscleGroupMap[muscleGroup]!!
                    )
                }

                composable(route = WorkoutScreen.MuscleGroupSelection.route) {
                    MuscleGroupSelectionScreen(
                        innerPadding = innerPadding,
                        onMuscleGroupClick = { muscleGroup ->
                            val selectMuscle =
                                "${WorkoutScreen.ExerciseSelection.route}/$muscleGroup"
                            navController.navigate(selectMuscle)
                        },
                        muscleGroups = muscleGroups
                    )
                }

                composable(route = MenuScreen.Main.route) {
                    MainMenuScreen(
                        innerPadding = innerPadding,
                        viewModel = MainMenuViewModel(),
                        onNewWorkoutClick = { navController.navigate(WorkoutScreen.Main.route) }
                    )
                }
            }
        }

    }
}