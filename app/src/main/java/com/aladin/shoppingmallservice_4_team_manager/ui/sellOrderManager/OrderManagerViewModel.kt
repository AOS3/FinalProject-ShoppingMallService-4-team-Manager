package com.aladin.shoppingmallservice_4_team_manager.ui.sellOrderManager

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aladin.shoppingmallservice_4_team_manager.model.OrderManagerModel
import com.aladin.shoppingmallservice_4_team_manager.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OrderManagerViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    // 구매 목록 상태 변경 확인 값
    private val _isLoadUserOrderInquiryData = MutableLiveData<Boolean>(false)
    val isLoadUserOrderInquiryData: LiveData<Boolean> get() = _isLoadUserOrderInquiryData

    // 구매 주문 관리 리스트
    private val _userOrderList = MutableLiveData<List<OrderManagerModel>>()
    val userOrderList: LiveData<List<OrderManagerModel>> get() = _userOrderList

    // 배송 상태 변경 확인 값
    private val _isFinishChangeDeliveryValue = MutableLiveData<Boolean>(false)
    val isFinishChangeDeliveryValue: LiveData<Boolean> get() = _isFinishChangeDeliveryValue

    // 주문 정보 가져오기
    fun gettingUserOrderInquiryData() {
        viewModelScope.launch {
            val result = runCatching {
                withContext(Dispatchers.IO) {
                    orderRepository.gettingOrderInquiryTableData()
                }
            }

            result
                .onSuccess { userOrderData ->
                    _userOrderList.postValue(userOrderData)
                    _isLoadUserOrderInquiryData.postValue(true)
                }
                .onFailure { error ->
                    Log.e("orderManagerViewModel", "gettingUserOrderInquiryData Error : $error")
                    _isLoadUserOrderInquiryData.postValue(true)
                }
        }
    }

    // 배송 상태 변경
    fun changeDeliveryStatus(
        orderInquiryNumber: String,
        orderInquiryTime: Long,
        deliveryValue: Int
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = runCatching {
                    withContext(Dispatchers.IO) {
                        orderRepository.changeOrderInquiryTableData(
                            orderInquiryNumber,
                            orderInquiryTime,
                            deliveryValue
                        )
                    }
                }

                result
                    .onSuccess {
                        _isFinishChangeDeliveryValue.postValue(true)
                    }
                    .onFailure { error ->
                        Log.e("orderManagerViewModel", "changeDeliveryStatus Error : $error")
                        _isFinishChangeDeliveryValue.postValue(true)
                    }
            }
        }
    }
}