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
class AuthViewmodel @Inject constructor(private val repository: CampaignRepository) : ViewModel() {
    fun authenticateUser(loginRequest: LoginRequest , context: Context ){
        viewModelScope.launch {
            try {
                repository.authenticate(loginRequest,context)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Authentication failed", e)
            }

        }


    }

}