package com.dilan.kamuda.houseownerapp.feature.home

import com.dilan.kamuda.houseownerapp.network.utils.OrderApiService
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val orderApiService: OrderApiService,
){

}