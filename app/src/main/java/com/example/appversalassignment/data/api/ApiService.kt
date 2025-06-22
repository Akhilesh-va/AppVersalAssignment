package com.example.appversalassignment.data.api

import com.example.appversalassignment.data.models.loginmodels.LoginRequest
import com.example.appversalassignment.data.models.loginmodels.LoginResponse
import com.example.appversalassignment.data.models.trackusermodels.TrackUserRequest
import com.example.appversalassignment.data.models.trackusermodels.TrackUserResponse
import com.example.appversalassignment.data.models.trackscreenmodels.TrackScreenRequest
import com.example.appversalassignment.data.models.trackscreenmodels.TrackScreenResponse
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/users/validate-account/")
    suspend fun validateAccount(@Body request: LoginRequest): LoginResponse
    @POST("api/v1/users/track-screen/")
    suspend fun trackScreen(
        @Header("Authorization") token: String,
        @Body request: TrackScreenRequest
    ): Response<TrackScreenResponse>

    @POST("api/v1/users/track-user/")
    suspend fun trackUser(
        @Header("Authorization") token: String,
        @Body request: TrackUserRequest
    ): Response<TrackUserResponse>
}