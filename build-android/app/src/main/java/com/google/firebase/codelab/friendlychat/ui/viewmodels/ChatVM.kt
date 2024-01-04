package com.example.campus.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatVM:ViewModel (){
    private var _darkMode = MutableStateFlow<Boolean>(false)
    val darkMode: StateFlow<Boolean> get() = _darkMode
    fun setDarkMode(value: Boolean){
        _darkMode.value = value
        Log.d("MarcusTAG2", "darkMode: ${_darkMode.value}")
    }
}

