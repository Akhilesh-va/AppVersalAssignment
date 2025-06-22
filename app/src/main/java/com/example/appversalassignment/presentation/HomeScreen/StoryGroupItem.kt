package com.example.appversalassignment.presentation.HomeScreen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appversalassignment.data.models.trackusermodels.Detail


@Composable
fun StoryGroupItem(
    detail: Detail,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .border(
                    width = 3.dp,
                    color = Color(android.graphics.Color.parseColor(detail.ringColor ?: "#ff0000")),
                    shape = CircleShape
                )
                .clip(CircleShape)
        ) {
            AsyncImage(
                model = detail.thumbnail,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = detail.name ?: "",
            color = Color(android.graphics.Color.parseColor(detail.nameColor ?: "#000000")),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
