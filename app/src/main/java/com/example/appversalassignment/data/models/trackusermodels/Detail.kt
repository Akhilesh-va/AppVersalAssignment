package com.example.appversalassignment.data.models.trackusermodels

data class Detail(
    val id: String? = null,
    val name: String? = null,
    val nameColor: String? = null,
    val order: Int? = null,
    val ringColor: String? = null,
    val slides: List<Slide?>? = null,
    val thumbnail: String? = null
)