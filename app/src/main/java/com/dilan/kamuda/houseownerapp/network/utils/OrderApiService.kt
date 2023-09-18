package com.dilan.kamuda.houseownerapp.network.utils

import com.dilan.kamuda.houseownerapp.constant.NetworkConstant
import com.dilan.kamuda.houseownerapp.feature.menu.model.FoodMenu
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface OrderApiService {
    @GET(NetworkConstant.ENDPOINT_MENU)
    suspend fun getMenuListForMeal(): Response<List<FoodMenu>>

    @PUT(NetworkConstant.ENDPOINT_MENU_UPDATE)
    fun updateMenuList(@Body list: List<FoodMenu>): Response<List<FoodMenu>?>
}