package com.dilan.kamuda.houseownerapp.feature.menu

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dilan.kamuda.houseownerapp.feature.menu.model.FoodMenu
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject
constructor(
    private val menuRepository: MenuRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _menuList = MutableLiveData<List<FoodMenu>>()
    val menuList: LiveData<List<FoodMenu>>
        get() = _menuList

    private val _checkedItems = MutableLiveData<List<FoodMenu>>()
    val checkedItems: LiveData<List<FoodMenu>>
        get() = _checkedItems

    private val _emptyOrder = MutableLiveData<Boolean>(true)
    val emptyOrder: LiveData<Boolean>
        get() = _emptyOrder

    private val _totalAmount = MutableLiveData<Boolean>(false)
    val totalAmount: LiveData<Boolean>
        get() = _totalAmount

    private val _resetList = MutableLiveData<Boolean>(false)
    val resetList: LiveData<Boolean>
        get() = _resetList


    fun getMenuListForMeal(meal: String) {
        viewModelScope.launch {

            var list = menuRepository.getMenuListForMealFromDataSource() ?: emptyList()
            _menuList.postValue(list)
        }
    }

    fun setCheckedItemsList(updatedCheckedItems: MutableList<FoodMenu>) {
        _emptyOrder.value = updatedCheckedItems.size < 1
        _checkedItems.value = updatedCheckedItems
    }

    fun updateMenuTable(myOrder: List<FoodMenu>) {

        viewModelScope.launch {
            //val res = menuRepository.updateMenuListInDataSource(myOrder)
        }
    }

//    fun calculateTotal() {
//        _totalAmount.value = true
//    }

    init {
        getMenuListForMeal("breakfast")
    }
}