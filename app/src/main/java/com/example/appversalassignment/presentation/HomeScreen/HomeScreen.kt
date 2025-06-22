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
import com.example.appversalassignment.viewmodel.CampaignViewmodel

@Composable
fun HomeScreen(
    campaignId: String,
    viewModel: CampaignViewmodel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    LaunchedEffect(campaignId) {
        viewModel.getCampaigns()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

            uiState.error != null -> Text(
                text = uiState.error ?: "Something went wrong",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )

            else -> {
                val campaign = uiState.campaigns.firstOrNull()
                campaign?.let {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Campaign ID: ${it.id}", style = MaterialTheme.typography.titleMedium)
                        Text("Type: ${it.campaign_type}")
                        Text("Stories: ${it.details}")
                    }
                } ?: Text("No campaign data found", Modifier.align(Alignment.Center))
            }
        }
    }
}
