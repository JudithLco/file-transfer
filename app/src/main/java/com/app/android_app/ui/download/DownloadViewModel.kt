package com.app.android_app.ui.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.android_app.data.model.FileResponse
import com.app.android_app.network.ApiService
import com.app.android_app.network.httpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

sealed class DownloadState {
    object Idle : DownloadState()
    object Loading : DownloadState()
    data class Success(val file: FileResponse, val data: ByteArray) : DownloadState()
    data class Error(val message: String) : DownloadState()
}

class DownloadViewModel : ViewModel() {
    private val api = ApiService(httpClient)

    private val _state = MutableStateFlow<DownloadState>(DownloadState.Idle)
    val state = _state.asStateFlow()

    fun downloadFile(fileId: String) {
        viewModelScope.launch {
            _state.value = DownloadState.Loading
            try {
                val (fileResponse, fileData) = api.downloadFile(fileId)
                _state.value = DownloadState.Success(fileResponse, fileData)
            } catch (e: Exception) {
                _state.value = DownloadState.Error(e.localizedMessage ?: "Download failed")
            }
        }
    }
}