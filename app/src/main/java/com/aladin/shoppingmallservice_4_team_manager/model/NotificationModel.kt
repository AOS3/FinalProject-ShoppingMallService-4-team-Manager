package com.aladin.shoppingmallservice_4_team_manager.model

data class NotificationModel (
    val notificationTitle : String = "",
    val notificationContent : String = "",
    val notificationUserToken : String = "",
    val notificationTime : Long = 0,
    val notificationState : Int = 0,
    val notificationSee : Int = 0
)