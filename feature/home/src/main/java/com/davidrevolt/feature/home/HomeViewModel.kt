package com.davidrevolt.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidrevolt.core.data.repository.BluetoothLowEnergyRepository
import com.davidrevolt.core.data.utils.snackbarmanager.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bluetoothLowEnergyRepository: BluetoothLowEnergyRepository,
    private val snackbarManager: SnackbarManager
) : ViewModel() {


    private val _isSyncing = MutableStateFlow(false)
    private val _stringsData = MutableStateFlow(listOf("David","Manshari"))

    val homeUiState = combine(_isSyncing, _stringsData) { isSyncing, stringsData ->
        HomeUiState.Data(isSyncing, stringsData)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState.Loading
        )

/*    val homeUiState = _isSyncing.map(HomeUiState::Data)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState.Loading
        )*/

    fun onSaveClick() {
        viewModelScope.launch {
            _isSyncing.value = true
            try {

            } catch (e: Exception) {
                Log.e("AppLog", "${e.message}")
                snackbarManager.snackbarMessage("${e.message}")
            }
            _isSyncing.value = false
        }
    }
}

sealed interface HomeUiState {
    data class Data(val isSyncing: Boolean, val stringsData:List<String>) :
        HomeUiState

    data object Loading : HomeUiState
}