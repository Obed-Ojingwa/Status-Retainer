// Main ViewModel

package com.nerdpace.statussaver.presentation.viewmodel


import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nerdpace.statussaver.domain.model.MediaType
import com.nerdpace.statussaver.domain.model.WhatsAppSource
import com.nerdpace.statussaver.domain.repository.StatusRepository
import com.nerdpace.statussaver.presentation.model.MainUiState
import com.nerdpace.statussaver.presentation.model.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: StatusRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        // Check if we have persistent SAF permissions
        checkSafPermissions()

        // Observe media changes
        observeMediaChanges()
    }

    fun onSourceChanged(source: WhatsAppSource) {
        _uiState.update { it.copy(selectedSource = source) }
        observeMediaChanges()

        // Check if we need SAF access for this source
        if (!hasSafPermission(source)) {
            _uiState.update { it.copy(needsSafAccess = true) }
        }
    }

    fun onTabChanged(tab: MediaType) {
        _uiState.update { it.copy(selectedTab = tab) }
        observeMediaChanges()
    }

    fun scanStatuses() {
        val currentSource = _uiState.value.selectedSource

        if (!hasSafPermission(currentSource)) {
            _uiState.update { it.copy(needsSafAccess = true) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isScanning = true, error = null) }

            repository.scanAndCacheStatuses(currentSource)
                .onSuccess {
                    _events.send(UiEvent.ShowMessage("Scan completed successfully"))
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message) }
                    _events.send(UiEvent.ShowMessage("Scan failed: ${error.message}"))
                }

            _uiState.update { it.copy(isScanning = false) }
        }
    }

    fun requestSafAccess(): Intent {
        val source = _uiState.value.selectedSource
        val uri = repository.getWhatsAppStatusUri(source)

        return Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            // Pre-select the WhatsApp status directory
            putExtra("android.provider.extra.INITIAL_URI", uri)
        }
    }

    fun onSafAccessGranted(uri: Uri) {
        val context = getApplication<Application>()

        // Take persistable permission
        val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION

        try {
            context.contentResolver.takePersistableUriPermission(uri, takeFlags)
            _uiState.update { it.copy(needsSafAccess = false) }

            // Now scan
            scanStatuses()
        } catch (e: Exception) {
            viewModelScope.launch {
                _events.send(UiEvent.ShowMessage("Failed to obtain permission: ${e.message}"))
            }
        }
    }

    private fun observeMediaChanges() {
        val source = _uiState.value.selectedSource
        val type = _uiState.value.selectedTab

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            repository.getStatusMedia(source, type)
                .catch { error ->
                    _uiState.update { it.copy(error = error.message, isLoading = false) }
                }
                .collect { mediaList ->
                    _uiState.update {
                        it.copy(
                            mediaList = mediaList,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    private fun checkSafPermissions() {
        val context = getApplication<Application>()
        val persistedUris = context.contentResolver.persistedUriPermissions

        // Check if we have persisted permissions for any WhatsApp directory
        val hasPermission = persistedUris.any { permission ->
            permission.uri.toString().contains("com.whatsapp") && permission.isReadPermission
        }

        _uiState.update { it.copy(needsSafAccess = !hasPermission) }
    }

    private fun hasSafPermission(source: WhatsAppSource): Boolean {
        val context = getApplication<Application>()
        val expectedUri = repository.getWhatsAppStatusUri(source)

        return context.contentResolver.persistedUriPermissions.any { permission ->
            permission.uri.toString().contains(
                when (source) {
                    WhatsAppSource.NORMAL_WHATSAPP -> "com.whatsapp/WhatsApp"
                    WhatsAppSource.WHATSAPP_BUSINESS -> "com.whatsapp.w4b"
                }
            ) && permission.isReadPermission
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
