package com.example.appversalassignment.data.models

import com.example.appversalassignment.data.models.trackusermodels.Campaign

data class CampaignUiState(
    val isLoading: Boolean = false,
    val campaigns: List<Campaign> = emptyList(),
    val error: String? = null
)
