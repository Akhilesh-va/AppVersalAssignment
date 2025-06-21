package com.example.appversalassignment.domain.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.appversalassignment.data.api.ApiService
import com.example.appversalassignment.data.models.loginmodels.LoginRequest
import jakarta.inject.Inject

class AuthenticationRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun authenticate(loginRequest: LoginRequest ,context: Context) {

        try {
            val response = apiService.validateAccount(loginRequest)
            Log.d("AuthenticationRepository", "Token: ${response.access_token}")
            val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("access_token", response.access_token).apply()

            Toast.makeText(context, "Token saved!", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Log.e("AuthenticationRepository", "Authentication failed", e)
            Toast.makeText(context, "Login failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }



}