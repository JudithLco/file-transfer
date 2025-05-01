package com.app.android_app.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.android_app.data.model.FileListResponse
import com.app.android_app.network.ApiService
import com.app.android_app.network.httpClient
import io.ktor.client.call.body
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ListState {
    object Idle : ListState()
    object Loading : ListState()
    data class Success(val response: FileListResponse) : ListState()
    data class Error(val message: String) : ListState()
}

class ListViewModel : ViewModel() {

    private val api = ApiService(httpClient)

    private val _state = MutableStateFlow<ListState>(ListState.Idle)
    val state = _state.asStateFlow()

    fun fetchAllFiles() {
        viewModelScope.launch {
            _state.value = ListState.Loading
            try {
                val response = api.fetchAllFiles()
                val fileListResponse = response.body<FileListResponse>()
                _state.value = ListState.Success(fileListResponse)
            } catch (e: Exception) {
                _state.value = ListState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}