package com.example.appversalassignment.presentation.campaignScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appversalassignment.data.models.loginmodels.trackusermodels.Campaign
import com.example.appversalassignment.viewmodel.CampaignViewmodel

import androidx.navigation.NavController

@Composable
fun CampaignIdScreen(
    navController: NavController,
    viewModel: CampaignViewmodel = hiltViewModel()
) {
    val uiState = viewModel.campaignIdUiState

    LaunchedEffect(Unit) {
        viewModel.getCampaignIds()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            uiState.error != null -> {
                Text(
                    text = uiState.error ?: "Something went wrong",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.campaignIds) { campaignId  ->
                        CampaignIdItem(
                            campaignId = campaignId ,
                            onClick = {
                                navController.navigate("home_screen/$campaignId ")
                            }
                        )
                    }
                }
            }
        }
    }
}