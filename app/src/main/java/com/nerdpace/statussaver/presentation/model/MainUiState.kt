// UI State Models

package com.nerdpace.statussaver.presentation.model

import com.nerdpace.statussaver.domain.model.MediaType
import com.nerdpace.statussaver.domain.model.StatusMedia
import com.nerdpace.statussaver.domain.model.WhatsAppSource


data class MainUiState(
    val selectedSource: WhatsAppSource = WhatsAppSource.NORMAL_WHATSAPP,
    val selectedTab: MediaType = MediaType.PHOTO,
    val mediaList: List<StatusMedia> = emptyList(),
    val isLoading: Boolean = false,
    val isScanning: Boolean = false,
    val error: String? = null,
    val hasStoragePermission: Boolean = false,
    val needsSafAccess: Boolean = false
)

data class PreviewUiState(
    val media: StatusMedia? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

sealed class UiEvent {
    data class ShowMessage(val message: String) : UiEvent()
    data class ShareMedia(val uri: android.net.Uri) : UiEvent()
    object NavigateBack : UiEvent()
}