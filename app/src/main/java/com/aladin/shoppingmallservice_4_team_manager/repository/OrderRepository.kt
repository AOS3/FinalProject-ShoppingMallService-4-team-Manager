package com.aladin.shoppingmallservice_4_team_manager.repository

import com.aladin.shoppingmallservice_4_team_manager.model.OrderManagerModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val firebaseFireStore: FirebaseFirestore
) {

    // 구매 정보 가져오기
    suspend fun gettingOrderInquiryTableData(): List<OrderManagerModel> {
        val collectionReference = firebaseFireStore.collection("OrderInquiryTable")

        val querySnapshot: QuerySnapshot = collectionReference
            .get()
            .await()

        val orderManagerList = querySnapshot.documents.mapNotNull { document ->
            document.toObject(OrderManagerModel::class.java)
        }

        return orderManagerList
    }

    // 배송 상태 변경
    suspend fun changeOrderInquiryTableData(orderInquiryNumber:String,orderInquiryTime:Long,deliveryValue:Int) {
        val collectionReference = firebaseFireStore.collection("OrderInquiryTable")

        val querySnapshot: QuerySnapshot = collectionReference
            .whereEqualTo("orderInquiryNumber",orderInquiryNumber)
            .whereEqualTo("orderInquiryTime",orderInquiryTime)
            .get()
            .await()

        querySnapshot.documents.forEach { document ->
            document.reference.update("orderInquiryDeliveryResult",deliveryValue)
        }
    }
}