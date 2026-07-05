package net.hawkins.gamescore.ui.leaderboard

import android.media.MediaPlayer
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import net.hawkins.gamescore.R
import net.hawkins.gamescore.data.model.Leaderboard
import net.hawkins.gamescore.data.model.toDataList
import net.hawkins.gamescore.ui.component.BackNavigationIcon
import net.hawkins.gamescore.ui.component.ShimmeringGoldText
import net.hawkins.gamescore.ui.theme.GameScoreTheme
import net.hawkins.gamescore.utils.isEven

@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel,
    onBack: () -> Unit,
    modifier: Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    viewModel.updateTopAppBar(
        newTitle = uiState.leaderboard.gameName + " - " + stringResource(R.string.leaderboard),
        newNavigationIcon = {
            BackNavigationIcon(onBack = onBack)
        }
    )

    LeaderboardScreenContent(
        uiState = uiState,
        modifier = modifier
    )
}

@Composable
private fun LeaderboardScreenContent(
    uiState: LeaderboardUiState,
    modifier: Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        val winner = uiState.leaderboard.winner
        if (winner != null) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                ShimmeringGoldText(
                    text = stringResource(R.string.player_wins, winner),
                    modifier = modifier
                )
            }
        }

        Row(modifier = modifier) {
            LeaderboardTable(
                headers = listOf(
                    stringResource(R.string.rank),
                    stringResource(R.string.player),
                    stringResource(R.string.score)
                ),
                data = uiState.leaderboard.toDataList(),
                hasWinner = winner != null,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun LeaderboardTable(
    headers: List<String>,
    data: List<List<String>>,
    hasWinner: Boolean,
    modifier: Modifier
) {
    val columnWidths = findColumnWidths(headers = headers, data = data)
    val (numberOfColumns, setNumberOfColumns) = remember { mutableIntStateOf(1) }

    val (displayFireworks, setDisplayFireworks) = remember { mutableStateOf(hasWinner) }
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val maxHeightInPx = constraints.maxHeight

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    // Get the actual height of the column content in pixels
                    val actualHeightInPx = coordinates.size.height

                    // Compare the actual height with the max available height
                    if (actualHeightInPx >= maxHeightInPx) {
                        setNumberOfColumns(2)
                    }
                }
        ) {
            if (numberOfColumns == 1) {
                LeaderboardColumn(
                    headers = headers,
                    data = data,
                    columnWidths = columnWidths,
                    hasWinner = hasWinner,
                    modifier = modifier
                )
            } else {
                Row()
                {
                    val chunkedSize = if (data.size.isEven()) {
                        data.size / 2
                    } else {
                        (data.size / 2) + 1
                    }
                    val chunkedData = data.chunked(chunkedSize)
                    LeaderboardColumn(
                        headers = headers,
                        data = chunkedData[0],
                        columnWidths = columnWidths,
                        hasWinner = hasWinner,
                        modifier = modifier
                    )
                    Spacer(modifier = modifier.width(50.dp))
                    LeaderboardColumn(
                        headers = headers,
                        data = chunkedData[1],
                        columnWidths = columnWidths,
                        hasWinner = hasWinner,
                        modifier = modifier
                    )
                }
            }
        }

        if (displayFireworks) {
            FireworksOverlay(
                onHideFireworks = {
                    setDisplayFireworks(false)
                },
                modifier = modifier
            )
        }
    }
}

@Composable
private fun FireworksOverlay(
    onHideFireworks: () -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.fireworks))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    val isPreview = LocalInspectionMode.current
    DisposableEffect(Unit) {
        if (isPreview) {
            onDispose { }
        } else {
            val mediaPlayer = MediaPlayer.create(context, R.raw.firework_sound)
            mediaPlayer?.apply {
                isLooping = true
                start()
            }
            onDispose {
                mediaPlayer?.apply {
                    if (isPlaying) stop()
                    release()
                }
            }
        }
    }

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Box(modifier = modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = {
                onHideFireworks()
            },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            modifier = modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.close))
        }
    }
}


private val Gold = Color(0xFFFFD700)
private val Silver = Color(0xFFE0E0E0) // Lightened from 0xFFC0C0C0 for better visibility on dark backgrounds
private val Bronze = Color(0xFFCD7F32)
private val Parchment = Color(0xFFF5F5DC) // Parchment for non-podium ranks

@Composable
private fun getSparkleBrush(rank: String): Brush? {
    val colors = when (rank) {
        "1" -> listOf(
            Color(0xFF8B4513), // Darker Gold/Bronze
            Gold,
            Color(0xFFFFECB3), // Lighter Gold (Sparkle)
            Gold,
            Color(0xFF8B4513)
        )
        "2" -> listOf(
            Color(0xFF707070), // Darker Silver
            Silver,
            Color(0xFFF5F5F5), // Lighter Silver (Sparkle)
            Silver,
            Color(0xFF707070)
        )
        "3" -> listOf(
            Color(0xFF804A00), // Darker Bronze
            Bronze,
            Color(0xFFE6BE8A), // Lighter Bronze (Sparkle)
            Bronze,
            Color(0xFF804A00)
        )
        else -> return null
    }

    val infiniteTransition = rememberInfiniteTransition(label = "sparkle")
    val translateAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "translate"
    )

    return Brush.linearGradient(
        colors = colors,
        start = Offset(translateAnim, translateAnim),
        end = Offset(translateAnim + 200f, translateAnim + 200f),
        tileMode = TileMode.Mirror
    )
}

@Composable
private fun getRankColor(rank: String): Color {
    val isDarkTheme = androidx.compose.foundation.isSystemInDarkTheme()
    return when (rank) {
        "1" -> Gold
        "2" -> Silver
        "3" -> Bronze
        else -> if (isDarkTheme) Parchment else Color.Unspecified
    }
}

@Composable
private fun LeaderboardColumn(
    headers: List<String>,
    data: List<List<String>>,
    columnWidths: List<Dp>,
    hasWinner: Boolean,
    modifier: Modifier
) {
    Column {
        Row(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            headers.forEachIndexed { index, header ->
                val boxWidth = when (index) {
                    0 -> columnWidths[0]
                    1 -> columnWidths[1]
                    else -> columnWidths[2]
                }
                Box(
                    modifier = modifier
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.surface,
                            shape = RectangleShape
                        )
                        .width(boxWidth)
                )
                {
                    Text(
                        text = header,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .padding(10.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Row {
            Column {
                data.forEach { rank ->
                    val rankColor = getRankColor(rank[0])
                    Box(
                        modifier = modifier
                            .width(columnWidths[0])
                    )
                    {
                        Text(
                            text = rank[0],
                            style = MaterialTheme.typography.headlineMedium,
                            color = if (hasWinner) rankColor else Color.Unspecified,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                data.forEach { rank ->
                    val sparkleBrush = if (hasWinner) getSparkleBrush(rank[0]) else null
                    val rankColor = getRankColor(rank[0])
                    Box(
                        modifier = modifier
                            .width(columnWidths[1])
                    ) {
                        Text(
                            text = rank[1],
                            style = if (sparkleBrush != null) {
                                MaterialTheme.typography.headlineMedium.copy(brush = sparkleBrush)
                            } else {
                                MaterialTheme.typography.headlineMedium
                            },
                            color = if (sparkleBrush == null) {
                                if (hasWinner) rankColor else Color.Unspecified
                            } else Color.Unspecified,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                }
            }
            Column {
                data.forEach { rank ->
                    val rankColor = getRankColor(rank[0])
                    Box(
                        modifier
                            .width(columnWidths[2])
                    ) {
                        Text(
                            text = rank[2],
                            style = MaterialTheme.typography.headlineMedium,
                            color = if (hasWinner) rankColor else Color.Unspecified,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun findColumnWidth(
    header: String,
    columnData: List<String>
): Dp {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    val all = columnData.plus(header)
    val widths = all.map {
        val textLayoutResult = textMeasurer.measure(
            style = MaterialTheme.typography.headlineMedium,
            text = it
        )
        with(density) {
            textLayoutResult.size.width.toDp()
        }
    }
    return widths.max()
}

@Composable
private fun findColumnWidths(
    headers: List<String>,
    data: List<List<String>>
): List<Dp> {
    val columnsData = mutableListOf<List<String>>()
    headers.forEachIndexed { index, _ ->
        val columnData = data.map { row -> row[index] }
        columnsData.add(columnData)
    }

    val columnWidths = headers.mapIndexed { index, header ->
        val columnWidth = findColumnWidth(header, columnsData[index])
        columnWidth * 1.3f
    }

    return columnWidths
}

@Preview
@Composable
private fun LeaderboardScreenContentPreview() {
    val gameName = "Seven Crowns"
    val rankings = listOf(
        Leaderboard.Ranking(1, 5, listOf("Sheldon")),
        Leaderboard.Ranking(2, 10, listOf("Penny")),
        Leaderboard.Ranking(3, 25, listOf("Leonard")),
        Leaderboard.Ranking(4, 27, listOf("Bernadette", "Howard")),
        Leaderboard.Ranking(6, 28, listOf("Amy")),
        Leaderboard.Ranking(7, 65, listOf("Rajesh"))
    )
    val leaderboard = Leaderboard(winner = "Sheldon", gameName = gameName, rankings = rankings)
    val uiState = LeaderboardUiState(leaderboard)

    LeaderboardScreenContent(
        uiState = uiState,
        modifier = Modifier
    )
}
