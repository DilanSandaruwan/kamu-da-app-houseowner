package com.dilan.kamuda.houseownerapp.feature.add

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dilan.kamuda.houseownerapp.feature.menu.MenuRepository
import com.dilan.kamuda.houseownerapp.feature.menu.model.FoodMenu
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewViewModel @Inject
constructor(
    private val menuRepository: MenuRepository,
    application: Application
) : AndroidViewModel(application) {


    fun saveNewItem(item:FoodMenu){

        viewModelScope.launch {
            menuRepository.saveMenuItemInDataSource(item)
        }
    }
}