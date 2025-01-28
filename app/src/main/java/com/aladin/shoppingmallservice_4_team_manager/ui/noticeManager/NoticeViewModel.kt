package com.aladin.shoppingmallservice_4_team_manager.ui.noticeManager

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aladin.shoppingmallservice_4_team_manager.model.NoticeModel
import com.aladin.shoppingmallservice_4_team_manager.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository
) : ViewModel() {

    // 공지사항 상태 변경 확인 값
    private val _isLoadNoticeData = MutableLiveData<Boolean>(false)
    val isLoadNoticeData: LiveData<Boolean> get() = _isLoadNoticeData

    // 공지사항 리스트
    private val _noticeList = MutableLiveData<List<NoticeModel>>()
    val noticeList: LiveData<List<NoticeModel>> get() = _noticeList

    // 공지사항 변경 상태 확인 값
    private val _isEditNoticeData = MutableLiveData<Boolean>(false)
    val isEditNoticeData: LiveData<Boolean> get() = _isEditNoticeData

    // 공지사항 데이터 가져오기
    fun gettingNoticeInquiryData() {
        viewModelScope.launch {
            val result = runCatching {
                withContext(Dispatchers.IO) {
                    noticeRepository.gettingNoticeTableData()
                }
            }

            result
                .onSuccess { noticeData ->
                    _noticeList.postValue(noticeData)
                    _isLoadNoticeData.postValue(true)
                }
                .onFailure { error ->
                    Log.e("NoticeViewModel", "gettingNoticeInquiryData Error : $error")
                    _isLoadNoticeData.postValue(true)
                }
        }
    }

    // 공지사항 수정
    fun editNoticeData(
        oldNoticeTitle: String,
        oldNoticeDate: String,
        noticeTitle: String,
        noticeContent: String,
        noticeDate: String,
    ) {
        viewModelScope.launch {
            val result = runCatching {
                withContext(Dispatchers.IO) {
                    noticeRepository.editNoticeTableData(
                        oldNoticeTitle,
                        oldNoticeDate,
                        noticeTitle,
                        noticeContent,
                        noticeDate
                    )
                }
            }

            result
                .onSuccess {
                    _isEditNoticeData.postValue(true)
                }
                .onFailure { error ->
                    Log.e("NoticeViewModel", "gettingNoticeInquiryData Error : $error")
                    _isEditNoticeData.postValue(true)
                }
        }
    }
}