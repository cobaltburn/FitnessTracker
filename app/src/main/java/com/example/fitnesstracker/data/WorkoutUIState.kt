package com.example.fitnesstracker.data

import java.util.Date

data class WorkoutUIState(
    val name: String = "",
    val date: Date = Date(),
    val startTime: Date = Date(),
    val endTime: Date? = null,
    val notes: String = "",
    val bodyWeight: String = "",
    val exercises: List<Exercise> = listOf()
)


data class Exercise(
    val name: String,
    val sets: List<ExerciseSet> = listOf(ExerciseSet())
)

data class ExerciseSet(val weight: String = "", val reps: String = "", val notes: String = "")
