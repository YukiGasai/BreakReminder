package com.yukigasai.breakreminder.presentation

import android.content.Context
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import java.time.Instant

class MainScreenViewModel(private val prefsHelper: PrefsHelper) : ViewModel() {


    val running = mutableStateOf(false)
    val timeUntilNextBreak = mutableLongStateOf(0L)
    val nextBreakTime = mutableLongStateOf(0L)
    val nextTask = mutableStateOf("")
    val breakInterval = mutableFloatStateOf(prefsHelper.getBreakInterval().toFloat())


    suspend fun updateOnceASecond() {
        while (running.value) {
            val currentTime = Instant.now().toEpochMilli()
            val _timeUntilNextBreak = nextBreakTime.longValue - currentTime

            if (_timeUntilNextBreak <= 0) {
                nextBreakTime.longValue = prefsHelper.getNextBreakTime() ?: 0
            } else {
                timeUntilNextBreak.longValue = _timeUntilNextBreak / 1000
            }

            delay(1000)
        }
    }

    fun onStopClick(context: Context) {
        running.value = false
        nextBreakTime.longValue = 0
        AlarmHelper.cancelNextAlarm(context)
    }

    fun onStartClick(context: Context) {
        val newNextTask = prefsHelper.saveNextTask()
        prefsHelper.saveBreakInterval(breakInterval.floatValue.toLong())
        nextTask.value = newNextTask
        running.value = true
        AlarmHelper.scheduleNextAlarm(context, prefsHelper)
        nextBreakTime.longValue = 0
    }

}