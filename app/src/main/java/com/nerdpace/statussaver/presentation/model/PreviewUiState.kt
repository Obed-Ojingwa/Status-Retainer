package com.nerdpace.statussaver.presentation.model

import com.nerdpace.statussaver.domain.model.StatusMedia


data class PreviewUiState(
    val media: StatusMedia? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)