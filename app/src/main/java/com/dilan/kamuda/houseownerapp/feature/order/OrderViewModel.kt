package com.dilan.kamuda.houseownerapp.feature.order

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
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    application: Application
) : AndroidViewModel(application) {

    var currentlySelectedGroup = "accepted"

    // Define three MutableLists for different order statuses
    val pendingList = MutableLiveData<List<OrderDetail>>()
    val acceptedList = MutableLiveData<List<OrderDetail>>()
    val completedList = MutableLiveData<List<OrderDetail>>()

    private val _ordersList = MutableLiveData<List<OrderDetail>>()
    val ordersList: LiveData<List<OrderDetail>>
        get() = _ordersList

    private val _objectHasUpdated = MutableLiveData<OrderDetail?>()
    val objectHasUpdated: LiveData<OrderDetail?>
        get() = _objectHasUpdated

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    private val _showErrorPopup = MutableLiveData<KamuDaPopup?>(null)
    val showErrorPopup: LiveData<KamuDaPopup?> = _showErrorPopup

    private val _showErrorPage = MutableLiveData<Boolean>()
    val showErrorPage: LiveData<Boolean> = _showErrorPage

    fun getOrderDetails() {
        viewModelScope.launch {
            when (val response = orderRepository.getOrdersListFromDataSource()) {
                is ApiState.Success -> {
                    _ordersList.postValue(response.data!!)
                }

                is ApiState.Failure -> {
                    _showErrorPage.postValue(true)
                }

                is ApiState.Loading -> {}
            }
        }
    }

    fun getOrderById(orderId: Int) {
        viewModelScope.launch {
            val response = orderRepository.getOrderByIdFromDataSource(orderId)
        }
    }

    fun getOrdersByStatus(status: String) {
        viewModelScope.launch {
            val response = orderRepository.getOrdersListByStatusFromDataSource(status)
            _ordersList.postValue(response)
        }
    }

    fun updateOrderWithStatus(orderId: Int, status: String) {
        _showLoader.value = true
        viewModelScope.launch {
            when (val response =
                orderRepository.updateOrderByIdWithStatusOnDataSource(orderId, status)) {
                is ApiState.Success -> {
                    _showLoader.postValue(false)
                    val kamuDaPopup = KamuDaPopup(
                        "Success",
                        "Order status was updated successfully!",
                        "",
                        "OK",
                        1
                    )
                    _objectHasUpdated.postValue(response.data)
                    _showErrorPopup.postValue(kamuDaPopup)
                }

                is ApiState.Failure -> {
                    _showLoader.postValue(false)
                    val kamuDaPopup = KamuDaPopup(
                        "Error",
                        "Failed to update the status",
                        "",
                        "Cancel",
                        2
                    )
                    _objectHasUpdated.postValue(null)
                    _showErrorPopup.postValue(kamuDaPopup)
                }

                is ApiState.Loading -> {}
            }
        }
    }

    fun resetShowErrorPopup() {
        _showErrorPopup.value = null
    }

    init {
        //getOrderDetails()
    }
}