package com.aladin.shoppingmallservice_4_team_manager.repository

import com.aladin.shoppingmallservice_4_team_manager.model.BookCountModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryManagerRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun loadBookCount(): List<BookCountModel> {
        val collectionReference = firestore.collection("BookCountTable")

        val bookCount = collectionReference.get().await()

        return bookCount.documents.mapNotNull { document ->
            document.toObject(BookCountModel::class.java)
        }
    }
}