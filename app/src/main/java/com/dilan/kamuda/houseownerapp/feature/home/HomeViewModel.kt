package com.dilan.kamuda.houseownerapp.feature.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dilan.kamuda.customerapp.model.specific.KamuDaPopup
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

    fun getLatestOrderOfCustomerByStatus(status: String) {
        viewModelScope.launch {
            //_showLoader.postValue(true)
            when (val res = homeRepository.getOrderListFromDataSource(status)) {
                is ApiState.Success -> {
                    // _showLoader.postValue(false)
                    if (res.data != null) {
                        _latestOrder.postValue(res.data.minByOrNull {
                            it.id
                        })
                    } else {
                        _latestOrder.postValue(null)
                    }
                }

                is ApiState.Failure -> {

                    val kamuDaPopup = KamuDaPopup(
                        "Error",
                        if (res.msg.contains("after")) {
                            "Server Error or not founnd"
                        } else if (res.msg.contains("Failed to connect to /", ignoreCase = false)) {
                            "No internet connection"
                        } else {
                            res.msg.toString()
                        },
                        "Retry",
                        "Cancel",
                        2
                    )
                    _showErrorPopup.postValue(kamuDaPopup)
                    //_showLoader.postValue(false)
                }

                is ApiState.Loading -> {
                    //_showLoader.postValue(true)
                }
            }
        }
    }

    fun getOrdersListForAllFromDataSource() {
        viewModelScope.launch {
            //_showLoader.postValue(true)
            when (val res = homeRepository.getOrdersListForAllFromDataSource()) {
                is ApiState.Success -> {
                    // _showLoader.postValue(false)
                    if (res.data != null) {
                        _allOrders.postValue(res.data!!)
                    } else {
                        _allOrders.postValue(emptyList())
                    }
                }

                is ApiState.Failure -> {

                    val kamuDaPopup = KamuDaPopup(
                        "Error",
                        if (res.msg.contains("after")) {
                            "Server Error or not founnd"
                        } else if (res.msg.contains("Failed to connect to /", ignoreCase = false)) {
                            "No internet connection"
                        } else {
                            res.msg.toString()
                        },
                        "Retry",
                        "Cancel",
                        2
                    )
                    _showErrorPopup.postValue(kamuDaPopup)
                    //_showLoader.postValue(false)
                }

                is ApiState.Loading -> {
                    //_showLoader.postValue(true)
                }
            }
        }
    }
}