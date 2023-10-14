package com.dilan.kamuda.houseownerapp.feature.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dilan.kamuda.customerapp.model.specific.KamuDaPopup
import com.dilan.kamuda.houseownerapp.feature.order.model.OrderDetail
import com.dilan.kamuda.houseownerapp.network.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _latestOrder = MutableLiveData<OrderDetail?>()
    val latestOrder: LiveData<OrderDetail?>
        get() = _latestOrder

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    private val _showErrorPopup = MutableLiveData<KamuDaPopup>()
    val showErrorPopup: LiveData<KamuDaPopup> = _showErrorPopup

}