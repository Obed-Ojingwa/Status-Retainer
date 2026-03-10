package com.nerdpace.statussaver.presentation.viewmodel


// Preview ViewModel


import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.nerdpace.statussaver.domain.repository.StatusRepository
import com.nerdpace.statussaver.presentation.model.PreviewUiState
import com.nerdpace.statussaver.presentation.model.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class PreviewViewModel @Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val repository: StatusRepository
) : AndroidViewModel(application) {

    private val mediaId: String = savedStateHandle.get<String>("mediaId")
        ?: throw IllegalStateException("Media ID is required")

    private val _uiState = MutableStateFlow(PreviewUiState())
    val uiState: StateFlow<PreviewUiState> = _uiState.asStateFlow()

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadMedia()
    }

    private fun loadMedia() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val media = repository.getMediaById(mediaId)

            _uiState.update {
                it.copy(
                    media = media,
                    isLoading = false,
                    error = if (media == null) "Media not found" else null
                )
            }
        }
    }

    fun saveToDevice() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            repository.saveToDevice(mediaId)
                .onSuccess { uri ->
                    _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
                    _events.send(UiEvent.ShowMessage("Saved successfully"))
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            error = error.message
                        )
                    }
                    _events.send(UiEvent.ShowMessage("Save failed: ${error.message}"))
                }
        }
    }

    fun shareMedia() {
        val media = _uiState.value.media ?: return

        viewModelScope.launch {
            // Use cached URI for sharing
            val shareUri = media.cachedUri ?: media.uri
            _events.send(UiEvent.ShareMedia(shareUri))
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}