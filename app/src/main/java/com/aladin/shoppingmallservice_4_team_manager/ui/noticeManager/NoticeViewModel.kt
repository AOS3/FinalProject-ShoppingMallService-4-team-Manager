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

    // 공지사항 추가 상태 확인 값
    private val _isAddNoticeData = MutableLiveData<Boolean>(false)
    val isAddNoticeData: LiveData<Boolean> get() = _isAddNoticeData

    // 공지사항 상태변경 상태 확인 값
    private val _isEditNoticeState = MutableLiveData<Boolean>(false)
    val isEditNoticeState: LiveData<Boolean> get() = _isEditNoticeState

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

    // 공지사항 추가
    fun addNoticeData(
        noticeTitle: String,
        noticeContent: String,
    ) {
        viewModelScope.launch {
            val noticeModel =
                NoticeModel(noticeTitle, noticeContent, System.currentTimeMillis().toString(), 0)

            val result = runCatching {
                withContext(Dispatchers.IO) {
                    noticeRepository.addNoticeTableData(noticeModel)
                }
            }

            result
                .onSuccess {
                    _isAddNoticeData.postValue(true)
                }
                .onFailure { error ->
                    Log.e("NoticeViewModel", "addNoticeData Error : $error")
                    _isAddNoticeData.postValue(true)
                }
        }
    }

    // 공지사항 상태 변경
    fun changeNoticeState(
        noticeTitle: String,
        noticeDate: String,
        noticeState: Int
    ) {
        viewModelScope.launch {
            val result = runCatching {
                withContext(Dispatchers.IO) {
                    noticeRepository.editNoticeTableStateData(noticeTitle, noticeDate, noticeState)
                }
            }

            result
                .onSuccess {
                    _isEditNoticeState.postValue(true)
                }
                .onFailure {
                    _isEditNoticeState.postValue(false)
                }
        }
    }
}