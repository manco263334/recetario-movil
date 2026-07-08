package com.dmm.recetario.ui.core

import android.annotation.SuppressLint
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    @SuppressLint("VisibleForTests")
    protected val savedStateHandle = SavedStateHandle()
}