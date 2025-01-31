package com.aladin.shoppingmallservice_4_team_manager.ui.sellOrderManager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aladin.shoppingmallservice_4_team_manager.Model.BookCountModel
import com.aladin.shoppingmallservice_4_team_manager.Model.NotificationModel
import com.aladin.shoppingmallservice_4_team_manager.Model.SellingInquiryModel
import com.aladin.shoppingmallservice_4_team_manager.Model.UsedBookInventoryModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellManagerViewModel @Inject constructor(
    private val sellManagerRepository: SellManagerRepository
) : ViewModel() {

    private val _sellingInquiries = MutableLiveData<List<SellingInquiryModel>>()
    val sellingInquiries: LiveData<List<SellingInquiryModel>> get() = _sellingInquiries

    private val _selectedInquiry = MutableLiveData<SellingInquiryModel?>()
    val selectedInquiry: LiveData<SellingInquiryModel?> get() = _selectedInquiry

    private val _filteredInquiries = MutableLiveData<List<SellingInquiryModel>>()
    val filteredInquiries: LiveData<List<SellingInquiryModel>> get() = _filteredInquiries

    // 실시간 감지를 위한 리스너
    private var detailListener: ListenerRegistration? = null

    private var allInquiries: List<SellingInquiryModel> = emptyList() // 전체 데이터 저장용

    init {
        observeSellingInquiries()
    }

    // Firestore에서 모든 데이터 실시간 감지
    private fun observeSellingInquiries() {
        FirebaseFirestore.getInstance()
            .collection("SellingInquiryTable")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                snapshot?.let {
                    val inquiries = it.documents.mapNotNull { doc ->
                        doc.toObject(SellingInquiryModel::class.java)?.apply {
                            documentId = doc.id
                        }
                    }
                    allInquiries = inquiries // 전체 데이터 저장
                    _sellingInquiries.postValue(inquiries) // UI 업데이트
                    filterInquiries(0) // 기본값: 검수 중 데이터 표시
                }
            }
    }

    // 특정 판매 내역 실시간 감지
    fun observeSellingInquiryById(documentId: String) {
        detailListener?.remove() // 기존 리스너 제거 (중복 방지)

        detailListener = FirebaseFirestore.getInstance()
            .collection("SellingInquiryTable")
            .document(documentId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                snapshot?.toObject(SellingInquiryModel::class.java)?.let {
                    it.documentId = documentId
                    // UI 자동 업데이트
                    _selectedInquiry.postValue(it)
                }
            }
    }

    // 승인 상태를 "품질 검수 중"으로 변경
    fun updateApprovalResultTo1(documentId: String) {
        viewModelScope.launch {
            try {
                sellManagerRepository.updateApprovalResultTo1(documentId)
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }

    // 승인 상태를 "검수 완료"로 변경하고 품질 선택 저장
    fun updateApprovalResultTo2(documentId: String, choiceQuality: Int) {
        viewModelScope.launch {
            try {
                sellManagerRepository.updateApprovalResultTo2(documentId, choiceQuality)
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }

    // Firestore의 `sellingInquiryOriginalPrice` 업데이트 (UI에는 영향 X)
    fun saveOriginalPriceIfNeeded(documentId: String, estimatedPrice: Int, quality: Int) {
        viewModelScope.launch {
            try {
                val originalPrice = calculateOriginalPrice(estimatedPrice, quality)

                // Firestore에서 원래 가격이 0일 경우만 업데이트 실행
                val inquiry = sellManagerRepository.getSellingInquiryById(documentId)
                if (inquiry?.sellingInquiryOriginalPrice == 0) {
                    sellManagerRepository.updateOriginalPrice(documentId, originalPrice)
                }
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }

    // 정상가 계산 함수
    private fun calculateOriginalPrice(estimatedPrice: Int, quality: Int): Int {
        return when (quality) {
            0 -> (estimatedPrice / 0.7).toInt()
            1 -> (estimatedPrice / 0.5).toInt()
            2 -> (estimatedPrice / 0.3).toInt()
            else -> 0
        }
    }

    // Firestore의 `sellingInquiryFinalPrice` 업데이트 (OriginalPrice 기반)
    fun updateFinalPriceAndWait(documentId: String, choiceQuality: Int, onComplete: (SellingInquiryModel?) -> Unit) {
        viewModelScope.launch {
            try {
                // Firestore에서 `OriginalPrice` 가져오기
                val inquiry = sellManagerRepository.getSellingInquiryById(documentId)

                inquiry?.let {
                    val originalPrice = it.sellingInquiryOriginalPrice
                    val finalPrice = calculateFinalPrice(originalPrice, choiceQuality)

                    // Firestore에서 최종 가격 업데이트
                    sellManagerRepository.updateFinalPrice(documentId, finalPrice)

                    // Firestore 업데이트가 완료된 후 최신 데이터 가져오기
                    waitForFirestoreUpdate(documentId, onComplete)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(null)
            }
        }
    }

    // OriginalPrice를 기반으로 최종 가격 계산
    private fun calculateFinalPrice(originalPrice: Int, quality: Int): Int {
        return when (quality) {
            0 -> (originalPrice * 0.7).toInt()
            1 -> (originalPrice * 0.5).toInt()
            2 -> (originalPrice * 0.3).toInt()
            else -> originalPrice
        }
    }

    // 선택된 상태에 따라 RecyclerView 필터링
    fun filterInquiries(filterType: Int) {
        val filteredList = when (filterType) {
            0 -> allInquiries.filter { it.sellingInquiryApprovalResult == 0 || it.sellingInquiryApprovalResult == 1 } // 검수 중
            1 -> allInquiries.filter { it.sellingInquiryApprovalResult == 2 } // 검수 완료
            else -> allInquiries // 기본: 전체 보기
        }
        _filteredInquiries.postValue(filteredList)
    }

    fun insertUsedBookInventory(inquiry: SellingInquiryModel) {
        viewModelScope.launch {
            try {
                val usedBook = UsedBookInventoryModel(
                    usedBookSellingPrice = inquiry.sellingInquiryFinalPrice,
                    usedBookQuality = inquiry.sellingInquiryChoiceQuality,
                    usedBookISBN = inquiry.sellingInquiryISBN,
                    usedBookTitle = inquiry.sellingInquiryBookName,
                    usedBookAuthor = inquiry.sellingInquiryBookAuthor,
                    usedBookTime = System.currentTimeMillis(), // 현재 시간 저장
                    usedBookState = 0 // 기본값 0
                )

                // Repository에서 Firestore 저장 처리
                sellManagerRepository.insertUsedBookInventory(usedBook)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun waitForFirestoreUpdate(documentId: String, onComplete: (SellingInquiryModel?) -> Unit) {
        viewModelScope.launch {
            try {
                // Firestore에서 최신 데이터 가져오기
                val updatedInquiry = sellManagerRepository.getSellingInquiryById(documentId)
                onComplete(updatedInquiry)
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(null)
            }
        }
    }

    fun insertNotification(inquiry: SellingInquiryModel) {
        viewModelScope.launch {
            try {
                val qualityText = getQualityText(inquiry.sellingInquiryChoiceQuality)

                val notification = NotificationModel(
                    notificationTitle = "\"${inquiry.sellingInquiryBookName}\" 품질 검수가 완료 되었습니다.",
                    notificationContent = "최종 품질 : $qualityText, 최종 가격 : ${inquiry.sellingInquiryFinalPrice}원",
                    notificationUserToken = inquiry.sellingInquiryUserToken,
                    notificationTime = System.currentTimeMillis(),
                    notificationState = 0,
                    notificationSee = 0
                )

                // Repository에서 Firestore 저장 처리
                sellManagerRepository.insertNotification(notification)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getQualityText(quality: Int): String {
        return when (quality) {
            0 -> "상"
            1 -> "중"
            2 -> "하"
            else -> "알 수 없음"
        }
    }

    fun insertBookCount(inquiry: SellingInquiryModel) {
        viewModelScope.launch {
            try {
                val firestore = FirebaseFirestore.getInstance()

                val bookCountRef = firestore.collection("BookCountTable")
                    .whereEqualTo("bookCountISBN", inquiry.sellingInquiryISBN)
                    .whereEqualTo("bookCountQuality", inquiry.sellingInquiryChoiceQuality)

                bookCountRef.get().addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        // 기존 데이터 존재 → `bookCountTotal` 증가
                        val document = querySnapshot.documents[0]
                        val currentTotal = document.getLong("bookCountTotal") ?: 0

                        firestore.collection("BookCountTable")
                            .document(document.id)
                            .update("bookCountTotal", currentTotal + 1)
                            .addOnSuccessListener {
                            }
                    } else {
                        // 기존 데이터 없음 → 새로 추가
                        val bookCount = BookCountModel(
                            bookCountISBN = inquiry.sellingInquiryISBN,
                            bookCountName = inquiry.sellingInquiryBookName,
                            bookCountAuthor = inquiry.sellingInquiryBookAuthor,
                            bookCountQuality = inquiry.sellingInquiryChoiceQuality,
                            bookCountTotal = 1, // 최초 추가 시 1로 설정
                            bookCountTime = System.currentTimeMillis(),
                            bookCountState = 0
                        )

                        firestore.collection("BookCountTable")
                            .add(bookCount)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        detailListener?.remove()
    }
}