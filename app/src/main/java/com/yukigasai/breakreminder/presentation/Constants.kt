package com.yukigasai.breakreminder.presentation


object Constants {

    object Notification {
        const val CHANNEL_ID = "break_reminder"
        const val ID = 1
    }

    object Preferences {
        const val NAME = "break_reminder"
        const val NEXT_TASK = "next_task"
        const val NEXT_ALARM_TIME = "next_alarm_time"
        const val BREAK_INTERVAL = "break_interval"
    }

    val TASK_LIST = listOf(
        "Stand up and stretch",
        "Grab a glass of water",
        "Take a walk around the room",
        "10 push-ups",
        "10 squats",
        "10 crunches",
        "Close your eyes and take a deep breath",
        "Look out the window",
    )
}
