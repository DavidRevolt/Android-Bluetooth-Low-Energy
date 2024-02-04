package com.davidrevolt.core.data.di


import com.davidrevolt.core.data.repository.BluetoothLowEnergyRepository
import com.davidrevolt.core.data.repository.BluetoothLowEnergyRepositoryImpl
import com.davidrevolt.core.data.utils.snackbarmanager.SnackbarManager
import com.davidrevolt.core.data.utils.snackbarmanager.SnackbarManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    // Repos & Utils
    @Binds
    abstract fun bindsBluetoothLowEnergyRepository(bluetoothLowEnergyRepositoryImpl: BluetoothLowEnergyRepositoryImpl): BluetoothLowEnergyRepository

    @Binds
    abstract fun bindsSnackbarManager(snackbarManagerImpl: SnackbarManagerImpl): SnackbarManager

}