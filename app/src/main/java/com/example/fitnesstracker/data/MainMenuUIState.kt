package com.example.fitnesstracker.data

import java.util.Date

data class MainMenuUIState(val workoutHistory: List<WorkoutHistory> = listOf())

data class WorkoutHistory(val day: Date, val name: String, val exercises: List<Pair<Int, String>>)