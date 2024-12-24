package com.yukigasai.breakreminder.presentation

import android.content.Context

class PrefsHelper(context: Context) {

    val sharedPreferences =
        context.getSharedPreferences(Constants.Preferences.NAME, Context.MODE_PRIVATE)

    fun getNextTask(): String {
        return sharedPreferences.getString(Constants.Preferences.NEXT_TASK, Constants.TASK_LIST[0])
            ?: Constants.TASK_LIST[0]
    }

    fun saveNextTask(): String {
        val nextTask = Constants.TASK_LIST.random()
        sharedPreferences.edit().putString(Constants.Preferences.NEXT_TASK, nextTask).apply()
        return nextTask
    }

    fun getNextBreakTime(): Long? {
        val nextAlarmTime = sharedPreferences.getLong(Constants.Preferences.NEXT_ALARM_TIME, 0)
        return if (nextAlarmTime != 0L) nextAlarmTime else null
    }

    fun saveNextBreakTime(time: Long?) {
        if (time == null) {
            sharedPreferences.edit().remove(Constants.Preferences.NEXT_ALARM_TIME).apply()
            return
        }
        sharedPreferences.edit().putLong(Constants.Preferences.NEXT_ALARM_TIME, time).apply()
    }

    fun getBreakInterval(): Long {
        val interval = sharedPreferences.getLong(Constants.Preferences.BREAK_INTERVAL, 15)
        return interval.coerceIn(1, 60)
    }

    fun saveBreakInterval(interval: Long) {
        sharedPreferences.edit().putLong(Constants.Preferences.BREAK_INTERVAL, interval).apply()
    }
}


