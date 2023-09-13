package com.wheeler.game.ui.wheeler

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wheeler.game.core.library.GameViewModel
import com.wheeler.game.core.library.random
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class WheelerViewModel : GameViewModel() {
    private val _spins = MutableLiveData(7)
    val spins: LiveData<Int> = _spins

    private val _bigWheelRotation = MutableLiveData(0f)
    val bigWheelRotation: LiveData<Float> = _bigWheelRotation

    private val _smallWheelRotation = MutableLiveData(0f)
    val smallWheelRotation: LiveData<Float> = _smallWheelRotation

    private val _state = MutableLiveData(1)
    val state: LiveData<Int> = _state

    private val _scores = MutableLiveData(0)
    val scores: LiveData<Int> = _scores

    private val _stateText = MutableLiveData("")
    val stateText: LiveData<String> = _stateText

    var isSpinning = false

    fun spin() {
        isSpinning = true
        _state.postValue(2)
        _spins.postValue(_spins.value!! - 1)
        viewModelScope.launch {
            var value = (8..25).random().toFloat()
            val randomDelay = 10 random 13
            val randomDecrease = Random.nextFloat() * (0.044f - 0.067f) + 0.084f

            var value2 = (8..25).random().toFloat()
            val randomDecrease2 = Random.nextFloat() * (0.044f - 0.067f) + 0.084f
            while (value > 0.10 || value2 > 0.10) {
                delay(randomDelay.toLong())
                if (value > 0.10f) {
                    value -= randomDecrease
                    _bigWheelRotation.postValue(_bigWheelRotation.value!! + value)
                }
                if (value2 > 0.10f) {
                    value2 -= randomDecrease2
                    _smallWheelRotation.postValue(_smallWheelRotation.value!! + value2)
                }
                if (value - randomDecrease < 0.10f && value2 - randomDecrease2 < 0.10f) {
                    val values = listOf(
                        0, 1_000_000, -1, 10, 25, 50, 250, 500, 80, 100, 1000, 0, 50, 500, 250, 25
                    )
                    val values2 = listOf(
                        1, 2, 0, 2, 1, 0, 2, 1
                    )
                    val totalSections = values.size
                    val degreesPerSection = 360f / totalSections
                    val normalizedDegrees = (_bigWheelRotation.value!!) % 360f

                    val totalSections2 = values2.size
                    val degreesPerSection2 = 360f / totalSections2
                    val normalizedDegrees2 = (_smallWheelRotation.value!!) % 360f

                    val sectionIndex = normalizedDegrees / degreesPerSection
                    val sectionIndex2 = normalizedDegrees2 / degreesPerSection2
                    if (isSpinning) {
                        isSpinning = false
                        checkWin(values[sectionIndex.toInt()], values2[sectionIndex2.toInt()] )
                    }
                }
            }
        }
    }

    private fun checkWin(droppedAmount: Int, multiplier: Int) {
        when (droppedAmount) {
            1_000_000 -> {
                _scores.postValue(_scores.value!! + 1000000)
                viewModelScope.launch {
                    _stateText.postValue("")
                    _state.postValue(3)
                    delay(1000)
                    _state.postValue(1)
                }
            }
            -1 -> {
                _scores.postValue(0)
                viewModelScope.launch {
                    _stateText.postValue("")
                    _state.postValue(4)
                    delay(1000)
                    _state.postValue(1)
                }
            }
            else -> {
                val totalWin = droppedAmount * multiplier
                _scores.postValue(_scores.value!! + totalWin)
                viewModelScope.launch {
                    _stateText.postValue("$droppedAmount points ${multiplier.toString() + "X"}")
                    _state.postValue(8)
                    delay(1000)
                    if (totalWin != 0) {
                        _stateText.postValue("win $totalWin")
                        _state.postValue(6)
                    } else {
                        _stateText.postValue("")
                        _state.postValue(5)
                    }
                    delay(1000)
                    _stateText.postValue("")
                    _state.postValue(1)
                }
            }
        }
    }
}