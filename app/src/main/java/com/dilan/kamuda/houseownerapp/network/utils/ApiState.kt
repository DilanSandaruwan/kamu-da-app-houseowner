package com.dilan.kamuda.houseownerapp.network.utils

sealed class ApiState<out T> {
    data class Success<out T>(val data: T) : ApiState<T>()
    data class Failure(val msg: String) : ApiState<Nothing>()
    object Loading : ApiState<Nothing>()
}
