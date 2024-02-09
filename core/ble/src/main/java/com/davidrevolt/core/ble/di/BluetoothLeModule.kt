package com.davidrevolt.core.ble.di

import android.content.Context
import com.davidrevolt.core.ble.BluetoothLe
import com.davidrevolt.core.ble.BluetoothLeImpl
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
        fun provideBluetoothLeScanner(@ApplicationContext context: Context): com.davidrevolt.core.ble.util.BluetoothLeScanner =
            com.davidrevolt.core.ble.util.BluetoothLeScanner(context)

    }
}