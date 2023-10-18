package com.dilan.kamuda.houseownerapp.feature.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dilan.kamuda.houseownerapp.common.util.KamuDaPopup
import com.dilan.kamuda.houseownerapp.feature.order.model.OrderDetail
import com.dilan.kamuda.houseownerapp.network.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _latestOrder = MutableLiveData<OrderDetail?>()
    val latestOrder: LiveData<OrderDetail?>
        get() = _latestOrder

    private val _allOrders = MutableLiveData<List<OrderDetail>>()
    val allOrders: LiveData<List<OrderDetail>>
        get() = _allOrders

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    private val _showErrorPopup = MutableLiveData<KamuDaPopup>()
    val showErrorPopup: LiveData<KamuDaPopup> = _showErrorPopup

    private val _showErrorPage = MutableLiveData<Boolean>()
    val showErrorPage: LiveData<Boolean> = _showErrorPage

    fun getOrdersListForAllFromDataSource() {
        _showErrorPage.value = false
        _showLoader.value = true
        viewModelScope.launch {
            when (val res = homeRepository.getOrdersListForAllFromDataSource()) {
                is ApiState.Success -> {
                    _showLoader.postValue(false)
                    if (res.data != null) {
                        _allOrders.postValue(res.data!!)
                    } else {
                        _allOrders.postValue(emptyList())
                    }
                }

                is ApiState.Failure -> {
                    _showLoader.postValue(false)
                    _showErrorPage.postValue(true)
                }

                is ApiState.Loading -> {

                }
            }
        }
    }
}