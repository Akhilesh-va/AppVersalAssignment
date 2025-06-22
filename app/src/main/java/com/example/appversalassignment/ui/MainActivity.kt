package com.example.appversalassignment.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appversalassignment.presentation.AuthenticationScreen
import com.example.appversalassignment.presentation.HomeScreen.HomeScreen
import com.example.appversalassignment.presentation.campaignScreens.CampaignIdScreen

import com.example.appversalassignment.ui.theme.AppVersalAssignmentTheme
import com.example.appversalassignment.viewmodel.AuthViewmodel
import com.example.appversalassignment.viewmodel.CampaignViewmodel
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val authViewmodel : AuthViewmodel = hiltViewModel()
            AppVersalAssignmentTheme {
                val navController = rememberNavController()
                val authViewmodel: AuthViewmodel = hiltViewModel()
                val campaignViewmodel: CampaignViewmodel = hiltViewModel()

                NavHost(
                    navController = navController,
                    startDestination = "auth"
                ) {
                    composable("auth") {
                        AuthenticationScreen(
                            authViewmodel = authViewmodel,
                            campaignViewmodel = campaignViewmodel,
                            onAuthenticated = {
                                navController.navigate("campaign_ids") {
                                    popUpTo("auth") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("campaign_ids") {
                        CampaignIdScreen(
                            navController = navController,
                            viewModel = campaignViewmodel
                        )
                    }
                    composable("home_screen/{campaignId}") { backStackEntry ->
                        val campaignId = backStackEntry.arguments?.getString("campaignId") ?: ""
                        HomeScreen(campaignViewmodel,navController)
                    }
                }

            }
        }
    }
}
