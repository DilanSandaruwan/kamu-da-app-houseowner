package com.dilan.kamuda.houseownerapp.feature.home

import android.util.Log
import com.dilan.kamuda.houseownerapp.feature.order.model.OrderDetail
import com.dilan.kamuda.houseownerapp.network.utils.ApiState
import com.dilan.kamuda.houseownerapp.network.utils.OrderApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val orderApiService: OrderApiService,
) {

    suspend fun getOrderListFromDataSource(status: String): ApiState<List<OrderDetail>?> {
        return withContext(Dispatchers.IO) {
            return@withContext getOrderListResponseFromRemoteService(status)
        }
    }


    private suspend fun getOrderListResponseFromRemoteService(status: String): ApiState<List<OrderDetail>?> {
        return try {
            val response = orderApiService.getOrdersListByStatus(status)
            if (response.isSuccessful) {
                ApiState.Success(response.body())
            } else {
                ApiState.Failure(response.message())
            }
        } catch (e: Exception) {
            ApiState.Failure(e.message.toString())
        }

    }

}