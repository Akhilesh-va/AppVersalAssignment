package com.example.appversalassignment.domain.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.example.appversalassignment.data.api.ApiService
import com.example.appversalassignment.data.models.loginmodels.LoginRequest
import com.example.appversalassignment.data.models.loginmodels.trackusermodels.Campaign
import com.example.appversalassignment.data.models.loginmodels.trackusermodels.TrackUserRequest
import com.example.appversalassignment.data.models.loginmodels.trackusermodels.TrackUserResponse
import com.example.appversalassignment.data.models.trackscreenmodels.TrackScreenRequest
import com.example.appversalassignment.data.models.trackscreenmodels.TrackScreenResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import retrofit2.Response

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
    suspend fun getCampaigns(): Result<List<Campaign>> {
        try {
            val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            val token = getAccessToken()
            Log.d("TokenCheck", "Token: $token")


            val screenResponse:Response<TrackScreenResponse> = apiService.trackScreen(
                "Bearer $token",
                TrackScreenRequest("Home Screen")

            )
            Log.d("TrackScreen", "isSuccessful: ${screenResponse.isSuccessful}")
            Log.d("TrackScreen", "body: ${screenResponse.body()}")
            Log.d("TrackScreen", "errorBody: ${screenResponse.errorBody()?.string()}")




            val campaignIds = screenResponse.body()?.campaigns
            Log.d("TrackScreen", "Campaign IDs: $campaignIds")



            val userResponse: Response<TrackUserResponse> = apiService.trackUser(
                "Bearer $token",
                TrackUserRequest(campaign_list = campaignIds)
            )
            Log.d("TrackUser", "Response body: ${userResponse.body()}")
            Log.d("TrackUser", "Raw: ${userResponse.code()} ${userResponse.message()}")


            return if (userResponse.isSuccessful) {
                val campaigns = userResponse.body()?.campaigns?.filterNotNull() ?: emptyList<Campaign>()
                Log.e("screenResponse",campaigns.toString())
                Result.success(campaigns)
            } else {
                Result.failure(Exception("Track user failed: ${userResponse.code()}"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}