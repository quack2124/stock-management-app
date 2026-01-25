package com.app.stockmanagement.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stockmanagement.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
) : ViewModel() {

    private val _loginEvent = Channel<LoginUiEvent>()
    val loginEvent = _loginEvent.receiveAsFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            if (username == Constants.USERNAME && password == Constants.PASSWORD) {
                _loginEvent.send(LoginUiEvent.Success)
            } else {
                _loginEvent.send(LoginUiEvent.Error(Constants.INVALID_CREDENTIALS))
            }
        }
    }

}

sealed class LoginUiEvent {
    data object Success : LoginUiEvent()
    data class Error(val message: String) : LoginUiEvent()
}