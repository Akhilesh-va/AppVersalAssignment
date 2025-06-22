package com.example.appversalassignment.domain.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.example.appversalassignment.data.api.ApiService
import com.example.appversalassignment.data.models.loginmodels.LoginRequest
import com.example.appversalassignment.data.models.trackusermodels.Campaign
import com.example.appversalassignment.data.models.trackusermodels.TrackUserRequest
import com.example.appversalassignment.data.models.trackscreenmodels.TrackScreenRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class CampaignRepository @Inject constructor(private val apiService: ApiService, @ApplicationContext private val context: Context) {
    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences("stories_prefs", Context.MODE_PRIVATE)

    fun saveAccessToken(token: String) {
        sharedPrefs.edit().putString("access_token", token).apply()
    }

    fun getAccessToken(): String? {
        return sharedPrefs.getString("access_token", null)
    }

    suspend fun authenticate(loginRequest: LoginRequest ,context: Context) {


        try {
            val response = apiService.validateAccount(loginRequest)
            Log.d("AuthenticationRepository", "Token: ${response.access_token}")
            saveAccessToken(response.access_token)

            Toast.makeText(context, "Token saved!", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Log.e("AuthenticationRepository", "Authentication failed", e)
            Toast.makeText(context, "Login failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun getCampaignIdsFromTrackScreen(): Result<List<String>> {
        return try {
            val token = getAccessToken()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("No access token found"))
            }

            val response = apiService.trackScreen(
                "Bearer $token",
                TrackScreenRequest(screen_name = "Home Screen")
            )

            if (response.isSuccessful) {
                val rawIds = response.body()?.campaigns
                val filteredIds = rawIds?.filterNotNull() ?: emptyList()
                Log.d("TrackScreen", "Filtered Campaign IDs: $filteredIds")

                Result.success(filteredIds)
            } else {
                val error = response.errorBody()?.string()
                Log.e("TrackScreen", "Error: $error")
                Result.failure(Exception("TrackScreen failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("TrackScreen", "Exception", e)
            Result.failure(e)
        }
    }


    suspend fun getCampaignsByIds(ids: List<String>): Result<List<Campaign>> {
        return try {
            val token = getAccessToken()
            if (token.isNullOrEmpty()) return Result.failure(Exception("No token found"))

            val response = apiService.trackUser(
                "Bearer $token",
                TrackUserRequest(campaign_list = ids)
            )

            if (response.isSuccessful) {
                val campaigns = response.body()?.campaigns?.filterNotNull() ?: emptyList()
                Log.d("TrackUser", "Campaigns: $campaigns")
                Result.success(campaigns)
            } else {
                val error = response.errorBody()?.string()
                Log.e("TrackUser", "Failed: $error")
                Result.failure(Exception("TrackUser failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}