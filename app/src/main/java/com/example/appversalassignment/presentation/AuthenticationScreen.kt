package com.example.appversalassignment.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appversalassignment.data.models.loginmodels.LoginRequest
import com.example.appversalassignment.viewmodel.AuthViewmodel




@Composable
fun AuthenticationScreen(
    authViewmodel: AuthViewmodel,
) {
    val context = LocalContext.current
    var appId by remember { mutableStateOf("") }
    var accountId by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Authentication", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = appId,
            onValueChange = { appId = it },
            label = { Text("App ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = accountId,
            onValueChange = { accountId = it },
            label = { Text("Account ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { authViewmodel.authenticateUser(LoginRequest("afadf960-3975-4ba2-933b-fac71ccc2002", "13555479-077f-445e-87f0-e6eae2e215c5"),context) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Authenticate")
        }
    }
}
