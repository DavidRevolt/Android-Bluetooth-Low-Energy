package com.davidrevolt.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidrevolt.core.data.repository.BluetoothLowEnergyRepository
import com.davidrevolt.core.data.utils.snackbarmanager.SnackbarManager
import com.davidrevolt.core.model.CustomScanResult
import dagger.hilt.android.lifecycle.HiltViewModel
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


    val homeUiState = combine(bluetoothLowEnergyRepository.isScanning(), bluetoothLowEnergyRepository.getScanResults()) { isScanning, scanResults ->
        HomeUiState.Data(isScanning = isScanning, scanResults = scanResults)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState.Loading
        )

    fun startBluetoothLeScan() {
        viewModelScope.launch {
            try {
                bluetoothLowEnergyRepository.startBluetoothLeScan()
            } catch (e: Exception) {
                Log.e("AppLog", "${e.message}")
                snackbarManager.snackbarMessage("${e.message}")
            }
        }
    }

    fun stopBluetoothLeScan() {
        viewModelScope.launch {
            try {
                bluetoothLowEnergyRepository.stopBluetoothLeScan()
            } catch (e: Exception) {
                Log.e("AppLog", "${e.message}")
                snackbarManager.snackbarMessage("${e.message}")
            }
        }
    }

}

sealed interface HomeUiState {
    data class Data(val isScanning: Boolean, val scanResults: List<CustomScanResult>) :
        HomeUiState

    data object Loading : HomeUiState
}