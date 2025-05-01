package com.app.android_app.ui.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.android_app.data.model.FileResponse
import com.app.android_app.data.model.FileUploadRequest
import com.app.android_app.network.ApiService
import com.app.android_app.network.httpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

sealed class UploadState {
    object Idle : UploadState()
    object Loading : UploadState()
    data class Success(val response: FileResponse) : UploadState()
    data class Error(val message: String) : UploadState()
}

class UploadViewModel : ViewModel() {

    private val api = ApiService(httpClient)

    private val _state = MutableStateFlow<UploadState>(UploadState.Idle)
    val state = _state.asStateFlow()

    fun uploadFile(file: File, deleteAfterUse: Boolean, expiration: Long) {
        viewModelScope.launch {
            _state.value = UploadState.Loading
            try {
                val request = FileUploadRequest(
                    deleteAfterUse = deleteAfterUse,
                    expiration = expiration
                )
                val response = api.uploadFile(file, request)
                _state.value = UploadState.Success(response)
            } catch (e: Exception) {
                _state.value = UploadState.Error(e.localizedMessage ?: "Erreur inconnue")
            }
        }
    }
}
