package com.aladin.shoppingmallservice_4_team_manager.model

data class OrderManagerModel (
    val orderInquiryPrice:Int = 0,
    val orderInquiryQuality:Int = 0,
    val orderInquiryISBN:String = "",
    val orderInquiryUserToken:String = "",
    val orderInquiryDeliveryResult:Int = 0,
    val orderInquiryTime:Long = 0L,
    val orderInquiryNumber:String = "",
    val orderInquiryTitle:String = "",
    val orderInquiryAuthor:String = "",
    val orderInquiryState:Int = 0,
    val orderInquiryName:String = "",
    val orderInquiryPhoneNumber:String = "",
    val orderInquiryAddress:String = "",
    val orderInquiryTotal:Int = 0,
)