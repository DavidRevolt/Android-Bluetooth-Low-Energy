package com.davidrevolt.core.ble.di

import android.content.Context
import com.davidrevolt.core.ble.BluetoothLe
import com.davidrevolt.core.ble.BluetoothLeImpl
import com.davidrevolt.core.ble.util.BluetoothLeConnectService
import com.davidrevolt.core.ble.util.BluetoothLeScanService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BluetoothLeModule {
    @Binds
    abstract fun bindsBluetoothLe(bluetoothLeImpl: BluetoothLeImpl): BluetoothLe

    companion object {
        @Provides
        @Singleton
        fun provideBluetoothLeScanService(@ApplicationContext context: Context): BluetoothLeScanService =
            BluetoothLeScanService(context)

        @Provides
        @Singleton
        fun provideBluetoothLeConnectService(@ApplicationContext context: Context): BluetoothLeConnectService =
            BluetoothLeConnectService(context)
    }
}