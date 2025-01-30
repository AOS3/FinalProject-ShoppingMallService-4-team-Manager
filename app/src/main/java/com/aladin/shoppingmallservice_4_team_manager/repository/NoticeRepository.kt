package com.aladin.shoppingmallservice_4_team_manager.repository

import com.aladin.shoppingmallservice_4_team_manager.model.NoticeModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoticeRepository @Inject constructor(
    private val firebaseFireStore: FirebaseFirestore
) {

    // 공지사항 정보 가져오기
    suspend fun gettingNoticeTableData(): List<NoticeModel> {
        val collectionReference = firebaseFireStore.collection("NoticeTable")

        val querySnapshot: QuerySnapshot = collectionReference
            .get()
            .await()

        val noticeManagerList = querySnapshot.documents.mapNotNull { document ->
            document.toObject(NoticeModel::class.java)
        }

        return noticeManagerList
    }

    // 공지사항 정보 수정
    suspend fun editNoticeTableData(
        oldNoticeTitle: String,
        oldNoticeDate: String,
        noticeTitle: String,
        noticeContent: String,
        noticeDate: String,
    ) {
        val collectionReference = firebaseFireStore.collection("NoticeTable")

        val querySnapshot: QuerySnapshot = collectionReference
            .whereEqualTo("noticeTitle", oldNoticeTitle)
            .whereEqualTo("noticeDate", oldNoticeDate)
            .get()
            .await()

        querySnapshot.documents.forEach { document ->
            document?.reference?.update("noticeTitle", noticeTitle)
            document?.reference?.update("noticeContent", noticeContent)
            document?.reference?.update("noticeDate", noticeDate)
        }
    }

    // 공지사항 정보 저장
    suspend fun addNoticeTableData(
        noticeModel: NoticeModel
    ) {
        val collectionReference = firebaseFireStore.collection("NoticeTable")
        collectionReference.add(noticeModel).await()
    }

    // 공지사항 상태 변경
    suspend fun editNoticeTableStateData(
        noticeTitle: String,
        noticeDate: String,
        noticeState: Int
    ) {
        val collectionReference = firebaseFireStore.collection("NoticeTable")

        val querySnapshot: QuerySnapshot = collectionReference
            .whereEqualTo("noticeTitle", noticeTitle)
            .whereEqualTo("noticeDate", noticeDate)
            .get()
            .await()

        querySnapshot.documents.forEach { document ->
            document?.reference?.update("noticeState", noticeState)
        }
    }
}