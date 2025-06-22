package com.example.appversalassignment.presentation.StoryViewerScreens

import androidx.media3.common.MediaItem
import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.appversalassignment.viewmodel.CampaignViewmodel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun StoryViewerScreen(
    storyId: String,
    viewModel: CampaignViewmodel = hiltViewModel(),
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val slides = viewModel.uiState.campaigns
        .flatMap { it.details?.filterNotNull() ?: emptyList() }
        .firstOrNull { it.id == storyId }?.slides?.filterNotNull() ?: emptyList()
    if (slides.isEmpty()) {
        LaunchedEffect(Unit) {
            onClose()
        }
        return
    }

    var currentSlideIndex by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState(initialPage = 0) { slides.size }
    val scope = rememberCoroutineScope()

    var player by remember { mutableStateOf<ExoPlayer?>(null) }

    DisposableEffect(currentSlideIndex) {
        val slide = slides[currentSlideIndex]
        player?.release()

        val videoUrl = slide.video
        if (!videoUrl.isNullOrBlank()) {
            val exoPlayer = ExoPlayer.Builder(context).build().apply {
                val uri = Uri.parse(videoUrl)
                val mediaItem = MediaItem.fromUri(uri)
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            }
            player = exoPlayer
            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_ENDED) {
                        if (currentSlideIndex < slides.lastIndex) {
                            scope.launch {
                                currentSlideIndex++
                                pagerState.animateScrollToPage(currentSlideIndex)
                            }
                        } else {
                            onClose()
                        }
                    }
                }
            })
        } else {
            scope.launch {
                delay(5000)
                if (currentSlideIndex < slides.lastIndex) {
                    currentSlideIndex++
                    pagerState.animateScrollToPage(currentSlideIndex)
                } else {
                    onClose()
                }
            }
        }

        onDispose {
            player?.release()
            player = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (it.x < size.width / 2) {
                            if (currentSlideIndex > 0) {
                                currentSlideIndex--
                                scope.launch {
                                    pagerState.animateScrollToPage(currentSlideIndex)
                                }
                            }
                        } else {
                            if (currentSlideIndex < slides.lastIndex) {
                                currentSlideIndex++
                                scope.launch {
                                    pagerState.animateScrollToPage(currentSlideIndex)
                                }
                            } else {
                                onClose()
                            }
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount > 50) onClose()
                }
            }
    ) {
        HorizontalPager(state = pagerState) { page ->
            val slide = slides[page]

            if (slide.video != null && player != null) {
                AndroidView(factory = {
                    PlayerView(context).apply {
                        this.player = player
                        useController = false
                    }
                }, modifier = Modifier.fillMaxSize())
            } else {
                AsyncImage(
                    model = slide.image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            slides.forEachIndexed { index, _ ->
                LinearProgressIndicator(
                    progress = when {
                        index < currentSlideIndex -> 1f
                        index == currentSlideIndex -> 0.5f
                        else -> 0f
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp),
                    color = Color.White
                )
            }
        }
    }
}
