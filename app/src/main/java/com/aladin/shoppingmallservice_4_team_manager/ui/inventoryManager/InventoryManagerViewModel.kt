package com.aladin.shoppingmallservice_4_team_manager.ui.inventoryManager

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aladin.shoppingmallservice_4_team_manager.model.BookCountModel
import com.aladin.shoppingmallservice_4_team_manager.repository.InventoryManagerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryManagerViewModel @Inject constructor(
    private val repository: InventoryManagerRepository
): ViewModel() {

    private val _inventoryList = MutableLiveData<List<BookCountModel>>()
    val inventoryList: LiveData<List<BookCountModel>> get() = _inventoryList

    fun loadList() = viewModelScope.launch {
        runCatching {
            repository.loadBookCount()
        }.onSuccess {
            _inventoryList.value = it
        }.onFailure {
            Log.d("inventoryViewModel", "error: $it")
        }
    }
}