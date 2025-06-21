package com.example.appversalassignment.data.api

import com.example.appversalassignment.data.models.loginmodels.LoginRequest
import com.example.appversalassignment.data.models.loginmodels.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/users/validate-account/")
    suspend fun validateAccount(@Body request: LoginRequest): LoginResponse
}