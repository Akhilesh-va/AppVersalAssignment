package com.example.appversalassignment.presentation.HomeScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.appversalassignment.viewmodel.CampaignViewmodel
@Composable
fun HomeScreen(viewModel: CampaignViewmodel = hiltViewModel(), navController: NavController) {
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.getCampaigns()
    }

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (uiState.error != null) {
        Text(
            text = uiState.error ?: "Unknown error",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )
    } else {
        Column {
            StoryGroupList(campaigns = uiState.campaigns) { detail ->
                // Navigate to fullscreen story viewer
                navController.navigate("story_viewer/${detail.id}")
            }
        }
    }
}