package com.davidrevolt.core.data.utils.snackbarmanager

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnackbarManagerImpl @Inject constructor() : SnackbarManager {

    //Send one time message.
    //Does not need an initial value so does not emit any value by default.
    //Does not store any data[Stateless].

    private val _message = MutableSharedFlow<String>()//Initial statues
    override val message = _message.asSharedFlow() //for screen to claim as read only

    override suspend fun snackbarMessage(snackbarMessage: String){
        _message.emit(snackbarMessage)
    }
}