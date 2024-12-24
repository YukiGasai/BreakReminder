package com.yukigasai.breakreminder.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent

object AlarmHelper {
    fun scheduleNextAlarm(context: Context, prefsHelper: PrefsHelper) {
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, BreakReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val interval = prefsHelper.getBreakInterval() * 60 * 1000
        val nextBreakTime = System.currentTimeMillis() + interval

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            nextBreakTime,
            pendingIntent
        )

        PrefsHelper(context).saveNextBreakTime(nextBreakTime)
    }

    fun cancelNextAlarm(context: Context) {
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, BreakReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        PrefsHelper(context).saveNextBreakTime(null)
    }
}