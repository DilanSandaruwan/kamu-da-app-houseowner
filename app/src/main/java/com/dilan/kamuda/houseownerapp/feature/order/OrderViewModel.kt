package com.dilan.kamuda.houseownerapp.feature.order

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dilan.kamuda.houseownerapp.feature.order.model.OrderDetail
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

    fun getOrderDetails() {
        viewModelScope.launch {
            val response = orderRepository.getOrdersListFromDataSource()
            _ordersList.postValue(response)
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
        viewModelScope.launch {
            val response = orderRepository.updateOrderByIdWithStatusOnDataSource(orderId, status)
            _objectHasUpdated.postValue(response)
        }
    }

    init {
        getOrderDetails()
    }
}