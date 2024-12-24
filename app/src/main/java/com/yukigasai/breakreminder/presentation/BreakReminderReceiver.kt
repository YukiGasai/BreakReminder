package com.yukigasai.breakreminder.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BreakReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        onBreakStarted(context)
    }

    private fun onBreakStarted(context: Context) {
        val prefsHelper = PrefsHelper(context)
        NotificationHelper.sendNotification(context, prefsHelper.getNextTask())
        prefsHelper.saveNextTask()
        AlarmHelper.scheduleNextAlarm(context, prefsHelper)
    }
}