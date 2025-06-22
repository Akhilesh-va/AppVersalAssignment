package com.example.appversalassignment.presentation.HomeScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items // Import the correct items function
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.appversalassignment.data.models.trackusermodels.Campaign
import com.example.appversalassignment.data.models.trackusermodels.Detail
@Composable
fun StoryGroupList(
    campaigns: List<Campaign>,
    onStoryGroupClick: (Detail) -> Unit
) {
    val storyGroups: List<Detail> = campaigns.flatMap { it.details?.filterNotNull() ?: emptyList() }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(items = storyGroups, key = { it.id ?: "" }) { detail ->
            StoryGroupItem(detail = detail) {
                onStoryGroupClick(detail)
            }
        }
    }
}
