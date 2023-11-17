package com.dilan.kamuda.houseownerapp.feature.menu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dilan.kamuda.houseownerapp.common.util.KamuDaPopup
import com.dilan.kamuda.houseownerapp.feature.menu.model.FoodMenu
import com.dilan.kamuda.houseownerapp.network.utils.ApiState
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

    private val _listChanged = MutableLiveData<Boolean>(false)
    val listChanged: LiveData<Boolean>
        get() = _listChanged

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    private val _showErrorPopup = MutableLiveData<KamuDaPopup?>()
    val showErrorPopup: LiveData<KamuDaPopup?> = _showErrorPopup

    private val _showErrorPage = MutableLiveData<Boolean>()
    val showErrorPage: LiveData<Boolean> = _showErrorPage

    fun getMenuListForMeal() {
        _showErrorPage.value = false
        _showLoader.value = true
        viewModelScope.launch {

            when (val list = menuRepository.getMenuListForMealFromDataSource()) {
                is ApiState.Success -> {
                    _showLoader.postValue(false)
                    _menuList.postValue(list.data!!)
                }

                is ApiState.Failure -> {
                    _showLoader.postValue(false)
                    _menuList.postValue(emptyList())
                    _showErrorPage.postValue(true)
                }

                is ApiState.Loading -> {}
            }

        }
    }

    fun setCheckedItemsList(updatedCheckedItems: MutableList<FoodMenu>) {
        _emptyOrder.value = updatedCheckedItems.size < 1
        _checkedItems.value = updatedCheckedItems
    }

    fun updateMenuTable(myOrder: List<FoodMenu>) {
        _showLoader.value = true
        viewModelScope.launch {
            when (val res = menuRepository.updateMenuListInDataSource(myOrder)) {
                is ApiState.Success -> {
                    if (res.data.isNullOrEmpty()) {

                    } else {
                        val kamuDaPopup = KamuDaPopup(
                            "Success",
                            "Menu list was updated successfully!",
                            "",
                            "OK",
                            1
                        )
                        _showErrorPopup.postValue(kamuDaPopup)
                        _listChanged.postValue(true)
                        _resetList.postValue(true)
                    }
                    _showLoader.postValue(false)
                }

                is ApiState.Failure -> {
                    _showLoader.postValue(false)
                    val kamuDaPopup = KamuDaPopup(
                        "Error",
                        if (res.msg != null || res.msg != "") {
                            res.msg
                        } else {
                            "Failed to update the menu list."
                        },
                        "",
                        "Cancel",
                        2
                    )
                    _showErrorPopup.postValue(kamuDaPopup)
                }

                is ApiState.Loading -> {}
            }
        }
    }

    fun resetErrorPopup() {
        _showErrorPopup.value = null
    }

    init {
        //getMenuListForMeal()
    }
}