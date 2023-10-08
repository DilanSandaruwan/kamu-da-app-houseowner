package com.dilan.kamuda.houseownerapp.feature.order

import com.dilan.kamuda.houseownerapp.feature.order.model.OrderDetail
import com.dilan.kamuda.houseownerapp.network.utils.OrderApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderApiService: OrderApiService,
) {
    // get all the orders as a list
    suspend fun getOrdersListFromDataSource(): List<OrderDetail> {
        return withContext(Dispatchers.IO) {
            return@withContext (getOrdersListFromRemoteSource())
        }
    }

    private suspend fun getOrdersListFromRemoteSource(): List<OrderDetail> {
        val response = orderApiService.getOrdersListForMeal()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }

    /***
     * GET the list of orders by the status
     */
    suspend fun getOrdersListByStatusFromDataSource(status: String): List<OrderDetail> {
        return withContext(Dispatchers.IO) {
            return@withContext (getOrdersListByStatusFromRemoteSource(status))
        }
    }

    private suspend fun getOrdersListByStatusFromRemoteSource(status: String): List<OrderDetail> {
        val response = orderApiService.getOrdersListByStateForMeal(status)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }

    /***
     * GET the list of orders by the order id
     */
    suspend fun getOrderByIdFromDataSource(orderId: Int): OrderDetail? {
        return withContext(Dispatchers.IO) {
            return@withContext (getOrderByIdFromRemoteSource(orderId))
        }
    }

    private suspend fun getOrderByIdFromRemoteSource(orderId: Int): OrderDetail? {
        val response = orderApiService.getOrderById(orderId)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    /***
     * UPDATE the order status
     */
    suspend fun updateOrderByIdWithStatusOnDataSource(orderId: Int, status: String): OrderDetail? {
        return withContext(Dispatchers.IO) {
            return@withContext (updateOrderByIdWithStatusOnRemoteSource(orderId, status))
        }
    }

    private suspend fun updateOrderByIdWithStatusOnRemoteSource(
        orderId: Int,
        status: String
    ): OrderDetail? {
        val response = orderApiService.updateOrderByIdWithStatus(orderId, status)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

}