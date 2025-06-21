package com.example.appversalassignment.domain.repository
// StoriesRepository.kt
import android.content.Context
import android.content.SharedPreferences
import com.example.appversalassignment.data.api.ApiService
import com.example.appversalassignment.data.models.loginmodels.trackusermodels.Campaign
import com.example.appversalassignment.data.models.loginmodels.trackusermodels.TrackUserRequest
import com.example.appversalassignment.data.models.trackscreenmodels.TrackScreenRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoriesRepository @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) {
    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences("stories_prefs", Context.MODE_PRIVATE)

    fun saveAccessToken(token: String) {
        sharedPrefs.edit().putString("access_token", token).apply()
    }

    fun getAccessToken(): String? {
        return sharedPrefs.getString("access_token", null)
    }

    suspend fun authenticate(appId: String, accountId: String): Flow<Result<String>> = flow {
        try {
            val response = apiService.validateAccount(AuthRequest(appId, accountId))
            if (response.isSuccessful) {
                val token = response.body()?.access_token
                if (token != null) {
                    saveAccessToken(token)
                    emit(Result.success(token))
                } else {
                    emit(Result.failure(Exception("Token is null")))
                }
            } else {
                emit(Result.failure(Exception("Authentication failed: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun getCampaigns(): Flow<Result<List<Campaign>>> = flow {
        try {
            val token = getAccessToken()
            if (token == null) {
                emit(Result.failure(Exception("No access token found")))
                return@flow
            }

            // Track Screen
            val screenResponse = apiService.trackScreen(
                "Bearer $token",
                TrackScreenRequest("Home Screen")
            )

            if (!screenResponse.isSuccessful) {
                emit(Result.failure(Exception("Track screen failed: ${screenResponse.code()}")))
                return@flow
            }

            val campaignIds = screenResponse.body()?.campaigns
            if (campaignIds.isNullOrEmpty()) {
                emit(Result.failure(Exception("No campaigns found")))
                return@flow
            }

            // Track User
            val userResponse = apiService.trackUser(
                "Bearer $token",
                TrackUserRequest(campaign_list = campaignIds)
            )

            if (userResponse.isSuccessful) {
                val campaigns = userResponse.body()?.campaigns ?: emptyList()
                emit(Result.success(campaigns) as Result<List<Campaign>>)
            } else {
                emit(Result.failure(Exception("Track user failed: ${userResponse.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
