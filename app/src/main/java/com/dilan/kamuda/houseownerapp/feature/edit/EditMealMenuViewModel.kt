package com.dilan.kamuda.houseownerapp.feature.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dilan.kamuda.houseownerapp.common.util.KamuDaPopup
import com.dilan.kamuda.houseownerapp.feature.menu.MenuRepository
import com.dilan.kamuda.houseownerapp.feature.menu.model.FoodMenu
import com.dilan.kamuda.houseownerapp.network.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMealMenuViewModel @Inject
constructor(
    private val menuRepository: MenuRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    private val _showErrorPopup = MutableLiveData<KamuDaPopup?>(null)
    val showErrorPopup: LiveData<KamuDaPopup?> = _showErrorPopup

    private val _showErrorPage = MutableLiveData<Boolean>()
    val showErrorPage: LiveData<Boolean> = _showErrorPage

    fun saveNewItem(item: FoodMenu) {
        _showErrorPage.value = false
        _showLoader.value = true
        viewModelScope.launch {
            when (val res = menuRepository.saveEditMenuItemInDataSource(item)) {
                is ApiState.Success -> {
                    _showLoader.postValue(false)
                    val kamuDaPopup = KamuDaPopup(
                        "Success",
                        "Menu was added successfully!",
                        "",
                        "OK",
                        1
                    )
                    _showLoader.postValue(false)
                    _showErrorPopup.postValue(kamuDaPopup)
                }

                is ApiState.Failure -> {
                    _showLoader.postValue(false)
                    val kamuDaPopup = KamuDaPopup(
                        "Error",
                        "Failed to add the menu",
                        "",
                        "Cancel",
                        2
                    )
                    _showLoader.postValue(false)
                    _showErrorPopup.postValue(kamuDaPopup)
                }

                is ApiState.Loading -> {}
            }
        }
    }

    fun resetErrorPopup() {
        _showErrorPopup.value = null
    }
}