package com.aladin.shoppingmallservice_4_team_manager.ui.sellOrderManager

import com.aladin.shoppingmallservice_4_team_manager.Model.BookCountModel
import com.aladin.shoppingmallservice_4_team_manager.Model.NotificationModel
import com.aladin.shoppingmallservice_4_team_manager.Model.SellingInquiryModel
import com.aladin.shoppingmallservice_4_team_manager.Model.UsedBookInventoryModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SellManagerRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    // Firestore에서 특정 판매 데이터를 가져오는 함수
    suspend fun getSellingInquiryById(documentId: String): SellingInquiryModel? {
        return try {
            val document = firestore.collection("SellingInquiryTable")
                .document(documentId)
                .get()
                .await()

            document.toObject(SellingInquiryModel::class.java)?.apply {
                this.documentId = documentId
            }
        } catch (e: Exception) {
            null
        }
    }

    // Firestore에서 `sellingInquiryApprovalResult` 값을 1로 변경
    suspend fun updateApprovalResultTo1(documentId: String) {
        try {
            firestore.collection("SellingInquiryTable")
                .document(documentId)
                .update("sellingInquiryApprovalResult", 1)
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    // Firestore에서 `sellingInquiryApprovalResult` 및 `sellingInquiryChoiceQuality` 값 변경 (2로 업데이트)
    suspend fun updateApprovalResultTo2(documentId: String, choiceQuality: Int) {
        try {
            firestore.collection("SellingInquiryTable")
                .document(documentId)
                .update(
                    mapOf(
                        "sellingInquiryApprovalResult" to 2,
                        "sellingInquiryChoiceQuality" to choiceQuality
                    )
                )
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    // Firestore에서 `sellingInquiryOriginalPrice` 업데이트
    suspend fun updateOriginalPrice(documentId: String, originalPrice: Int) {
        try {
            firestore.collection("SellingInquiryTable")
                .document(documentId)
                .update("sellingInquiryOriginalPrice", originalPrice)
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    // Firestore에서 `sellingInquiryFinalPrice` 업데이트
    suspend fun updateFinalPrice(documentId: String, finalPrice: Int) {
        try {
            firestore.collection("SellingInquiryTable")
                .document(documentId)
                .update("sellingInquiryFinalPrice", finalPrice)
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    // Firestore에 UsedBookInventoryTable에 저장
    suspend fun insertUsedBookInventory(usedBook: UsedBookInventoryModel) {
        try {
            firestore.collection("UsedBookInventoryTable")
                .add(usedBook)
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    // Firestore에 NotificationTable에 저장
    suspend fun insertNotification(notification: NotificationModel) {
        try {
            firestore.collection("NotificationTable")
                .add(notification)
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    // Firestore에 BookCountTable에 저장
    suspend fun insertBookCount(bookCount: BookCountModel) {
        try {
            firestore.collection("BookCountTable")
                .add(bookCount)
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

}
