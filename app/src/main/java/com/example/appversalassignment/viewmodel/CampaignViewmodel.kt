package com.example.appversalassignment.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appversalassignment.data.models.loginmodels.LoginRequest
import com.example.appversalassignment.domain.repository.CampaignRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CampaignViewmodel @Inject constructor(private val repository: CampaignRepository) : ViewModel() {

    fun getCampaigns() {
        viewModelScope.launch {
            try {
                val result = repository.getCampaigns()
                if (result.isSuccess) {
                    val campaigns = result.getOrNull()
                    Log.e("Campaigns", "Received: $campaigns")
                } else {
                    val exception = result.exceptionOrNull()
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Failed to get campaigns", e)
            }
        }}
}