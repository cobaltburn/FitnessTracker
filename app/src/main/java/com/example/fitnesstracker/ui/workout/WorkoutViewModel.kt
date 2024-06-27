package com.example.fitnesstracker.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.data.Exercise
import com.example.fitnesstracker.data.ExerciseSet
import com.example.fitnesstracker.data.WorkoutUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class WorkoutViewModel : ViewModel() {

    private val _workoutState = MutableStateFlow(WorkoutUIState())
    val workoutState: StateFlow<WorkoutUIState> = _workoutState

    fun setName(name: String) =
        _workoutState.update { it.copy(name = name) }

    fun setDate(date: Date) =
        _workoutState.update { it.copy(date = date) }

    fun setStartTime(startTime: Date) =
        _workoutState.update { it.copy(startTime = startTime) }

    fun setEndTime(endTime: Date) =
        _workoutState.update { it.copy(endTime = endTime) }

    fun setBodyWeight(bodyWeight: String) =
        _workoutState.update { it.copy(bodyWeight = bodyWeight) }


    fun setNotes(notes: String) =
        _workoutState.update { it.copy(notes = notes) }


    fun addExercise(exercise: Exercise) =
        _workoutState.update { it.copy(exercises = it.exercises + exercise) }


    fun setExercise(i: Int, exercise: Exercise) =
        viewModelScope.launch {
            _workoutState.update {
                val exercises = it.exercises.toMutableList()
                exercises[i] = exercise
                it.copy(exercises = exercises)
            }
        }


    fun addExerciseSet(i: Int, set: ExerciseSet) =
        viewModelScope.launch {
            _workoutState.update {
                val currentExercises = it.exercises.toMutableList()
                val currentExercise =
                    currentExercises[i].copy(sets = it.exercises[i].sets + set)
                currentExercises[i] = currentExercise
                it.copy(exercises = currentExercises)
            }
        }

}