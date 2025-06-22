package com.example.appversalassignment.data.models.trackusermodels

data class TrackUserResponse(
    val campaigns: List<Campaign?>? = null,
    val is_screen_capture_enabled: Boolean? = null,
    val is_test_user: Boolean? = null,
    val user_id: String? = null
)