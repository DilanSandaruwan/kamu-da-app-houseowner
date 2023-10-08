package com.dilan.kamuda.houseownerapp.network.utils

import com.dilan.kamuda.houseownerapp.constant.NetworkConstant
import com.dilan.kamuda.houseownerapp.feature.menu.model.FoodMenu
import com.dilan.kamuda.houseownerapp.feature.menu.model.FoodMenuGetType
import com.dilan.kamuda.houseownerapp.feature.order.model.OrderDetail
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrderApiService {
    @GET(NetworkConstant.ENDPOINT_MENU)
    suspend fun getMenuListForMeal(): Response<List<FoodMenuGetType>>

    @GET(NetworkConstant.ENDPOINT_GET_ORDERS_ALL)
    suspend fun getOrdersListForMeal(): Response<List<OrderDetail>>

    @GET(NetworkConstant.ENDPOINT_GET_ORDERS_BY_STATE)
    suspend fun getOrdersListByStateForMeal(@Path("status") status: String): Response<List<OrderDetail>>

    @GET(NetworkConstant.ENDPOINT_GET_ORDER_BY_ID)
    suspend fun getOrderById(@Path("orderId") orderId: Int): Response<OrderDetail>

    @PUT(NetworkConstant.ENDPOINT_PUT_ORDER)
    suspend fun updateOrderByIdWithStatus(
        @Path("id") orderId: Int,
        @Path("status") status: String
    ): Response<OrderDetail>

    @PUT(NetworkConstant.ENDPOINT_MENU_UPDATE)
    suspend fun updateMenuList(@Body list: List<FoodMenu>): Response<List<FoodMenu>?>

    @POST(NetworkConstant.ENDPOINT_MENU_SAVE)
    suspend fun saveMenuItem(@Body item: FoodMenu): Response<FoodMenu>
}