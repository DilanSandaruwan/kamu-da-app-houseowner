package com.dilan.kamuda.houseownerapp.feature.menu

import com.dilan.kamuda.houseownerapp.feature.menu.model.FoodMenu
import com.dilan.kamuda.houseownerapp.network.utils.OrderApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MenuRepository @Inject constructor(
    private val orderApiService: OrderApiService,
) {

    private val TAG = "MenuRepository"
    suspend fun getMenuListForMealFromDataSource(): List<FoodMenu>? {
        return withContext(Dispatchers.IO) {
            return@withContext getResponseFromRemoteService()
        }
    }

    private suspend fun getResponseFromRemoteService(): List<FoodMenu>? {
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
            return modifiedResponseBody
        }
        return emptyList()
    }

    suspend fun updateMenuListInDataSource(list: List<FoodMenu>): List<FoodMenu>? {
        return withContext(Dispatchers.IO) {
            return@withContext updateMenuListInRemoteSource(list)
        }
    }

    private suspend fun updateMenuListInRemoteSource(list: List<FoodMenu>): List<FoodMenu>? {
        val response = orderApiService.updateMenuList(list)
        if (response.isSuccessful) {
            return response.body()
        }
        return emptyList()
    }

    suspend fun saveMenuItemInDataSource(item: FoodMenu): FoodMenu? {
        return withContext(Dispatchers.IO) {
            return@withContext saveMenuItemInRemoteSource(item)
        }
    }

    private suspend fun saveMenuItemInRemoteSource(item: FoodMenu): FoodMenu? {
        val response = orderApiService.saveMenuItem(item)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }
}