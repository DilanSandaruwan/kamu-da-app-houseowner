package com.dilan.kamuda.houseownerapp.feature.menu

import com.dilan.kamuda.houseownerapp.feature.menu.model.FoodMenu
import com.dilan.kamuda.houseownerapp.network.utils.ApiState
import com.dilan.kamuda.houseownerapp.network.utils.OrderApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MenuRepository @Inject constructor(
    private val orderApiService: OrderApiService,
) {

    private val TAG = "MenuRepository"
    suspend fun getMenuListForMealFromDataSource(): ApiState<List<FoodMenu>?> {
        return withContext(Dispatchers.IO) {
            return@withContext getResponseFromRemoteService()
        }
    }

    private suspend fun getResponseFromRemoteService(): ApiState<List<FoodMenu>?> {
        try {
            val response = orderApiService.getMenuListForMeal()
            if (response.isSuccessful) {
                var modifiedResponseBody = mutableListOf<FoodMenu>()
                var originalResponseBody = response.body()
//            var bitmap: Bitmap? = null
                var bitmap: ByteArray?
                originalResponseBody?.let {
                    for (item in originalResponseBody) {

                        bitmap = if (item.image != null) {
                            val imageData =
                                android.util.Base64.decode(item.image, android.util.Base64.DEFAULT)
                            imageData
                            //BitmapFactory.decodeByteArray(imageData,0,imageData.size)
                        } else {
                            null
                        }
                        modifiedResponseBody.add(
                            FoodMenu(item.id, item.name, item.price, item.status, bitmap)
                        )
                    }
                }
                return ApiState.Success(modifiedResponseBody)
            } else {
                return ApiState.Failure("Something went wroong when loading this screen.")
            }
        } catch (exception: Exception) {
            return ApiState.Failure("Something went wroong when loading this screen.")
        }
    }

    suspend fun updateMenuListInDataSource(list: List<FoodMenu>): ApiState<List<FoodMenu>?> {
        return withContext(Dispatchers.IO) {
            return@withContext updateMenuListInRemoteSource(list)
        }
    }

    private suspend fun updateMenuListInRemoteSource(list: List<FoodMenu>): ApiState<List<FoodMenu>?> {
        return try {
            val response = orderApiService.updateMenuList(list)
            if (response.isSuccessful) {
                ApiState.Success(response.body())
            } else {
                ApiState.Failure("Failed to update the menu list")
            }
        } catch (exception: Exception) {
            ApiState.Failure("Failed to update the menu list")
        }
    }

    suspend fun saveMenuItemInDataSource(item: FoodMenu): ApiState<FoodMenu?> {
        return withContext(Dispatchers.IO) {
            return@withContext saveMenuItemInRemoteSource(item)
        }
    }

    private suspend fun saveMenuItemInRemoteSource(item: FoodMenu): ApiState<FoodMenu?> {
        return try {
            val response = orderApiService.saveMenuItem(item)
            if (response.isSuccessful) {
                ApiState.Success(response.body())
            } else {
                ApiState.Failure("Failed to save thr menu.")
            }
        } catch (exception: Exception) {
            ApiState.Failure(exception.message.toString())
        }
    }

    suspend fun saveEditMenuItemInDataSource(item: FoodMenu): ApiState<FoodMenu?> {
        return withContext(Dispatchers.IO) {
            return@withContext saveEditMenuItemInRemoteSource(item)
        }
    }

    private suspend fun saveEditMenuItemInRemoteSource(item: FoodMenu): ApiState<FoodMenu?> {
        return try {
            val response = orderApiService.updateEditMenuItem(item.id, item)
            if (response.isSuccessful) {
                ApiState.Success(response.body())
            } else {
                ApiState.Failure("Failed to save the updated menu.")
            }
        } catch (exception: Exception) {
            ApiState.Failure(exception.message.toString())
        }
    }
}