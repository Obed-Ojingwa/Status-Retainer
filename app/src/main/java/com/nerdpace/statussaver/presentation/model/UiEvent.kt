package com.nerdpace.statussaver.presentation.model


sealed class UiEvent {
    data class ShowMessage(val message: String) : UiEvent()
    data class ShareMedia(val uri: android.net.Uri) : UiEvent()
    object NavigateBack : UiEvent()
}