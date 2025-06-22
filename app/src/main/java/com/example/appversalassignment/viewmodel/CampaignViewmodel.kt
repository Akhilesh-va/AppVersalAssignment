package com.example.appversalassignment.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appversalassignment.data.models.CampaignUiState
import com.example.appversalassignment.data.models.loginmodels.LoginRequest
import com.example.appversalassignment.domain.repository.CampaignRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CampaignViewmodel @Inject constructor(
    private val repository: CampaignRepository
) : ViewModel() {
    data class CampaignIdUiState(
        val isLoading: Boolean = false,
        val campaignIds: List<String> = emptyList(),
        val error: String? = null
    )

    var uiState by mutableStateOf(CampaignUiState())
        private set
    var campaignIdUiState by mutableStateOf(CampaignIdUiState())
        private set
    fun getCampaignIds() {
        campaignIdUiState = campaignIdUiState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val result = repository.getCampaignIdsFromTrackScreen()
            if (result.isSuccess) {
                val ids = result.getOrNull() ?: emptyList()
                campaignIdUiState = campaignIdUiState.copy(
                    isLoading = false,
                    campaignIds = ids
                )
            } else {
                campaignIdUiState = campaignIdUiState.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.localizedMessage ?: "Failed to fetch campaign IDs"
                )
            }
        }
    }




    fun getCampaigns() {
        uiState = uiState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                // Step 1: Get campaign IDs from TrackScreen API
                val idsResult = repository.getCampaignIdsFromTrackScreen()

                if (idsResult.isFailure) {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = idsResult.exceptionOrNull()?.message ?: "Failed to get campaign IDs"
                    )
                    return@launch
                }

                val campaignIds = idsResult.getOrNull() ?: emptyList()

                if (campaignIds.isEmpty()) {
                    uiState = uiState.copy(
                        isLoading = false,
                        campaigns = emptyList(),
                        error = "No campaign IDs found"
                    )
                    return@launch
                }

                // Step 2: Fetch full campaign data using those IDs
                val campaignsResult = repository.getCampaignsByIds(campaignIds)

                if (campaignsResult.isSuccess) {
                    val campaigns = campaignsResult.getOrNull() ?: emptyList()
                    uiState = uiState.copy(
                        isLoading = false,
                        campaigns = campaigns,
                        error = null
                    )
                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = campaignsResult.exceptionOrNull()?.message ?: "Failed to fetch campaign data"
                    )
                }

            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Unexpected error"
                )
            }
        }
    }
}
