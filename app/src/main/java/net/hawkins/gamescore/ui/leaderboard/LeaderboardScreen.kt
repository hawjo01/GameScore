package net.hawkins.gamescore.ui.leaderboard

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.hawkins.gamescore.R
import net.hawkins.gamescore.data.model.Leaderboard
import net.hawkins.gamescore.data.model.toDataList
import net.hawkins.gamescore.ui.component.BackNavigationIcon
import net.hawkins.gamescore.ui.component.ShimmeringGoldText
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
                modifier = modifier
            )
        }
    }
}

@Composable
private fun LeaderboardTable(
    headers: List<String>,
    data: List<List<String>>,
    modifier: Modifier
) {
    val columnWidths = findColumnWidths(headers = headers, data = data)
    val (numberOfColumns, setNumberOfColumns) = remember { mutableIntStateOf(1) }

    // BoxWithConstraints provides the maximum space the Column can occupy
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
                    firstPlayerColor = Color.Green,
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
                        firstPlayerColor = Color.Green,
                        modifier = modifier
                    )
                    Spacer(modifier = modifier.width(50.dp))
                    LeaderboardColumn(
                        headers = headers,
                        data = chunkedData[1],
                        columnWidths = columnWidths,
                        firstPlayerColor = Color.Unspecified,
                        modifier = modifier
                    )
                }
            }
        }
    }
}

@Composable
private fun LeaderboardColumn(
    headers: List<String>,
    data: List<List<String>>,
    columnWidths: List<Dp>,
    firstPlayerColor: Color,
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
                data.forEachIndexed { index, rank ->
                    Box(
                        modifier = modifier
                            .width(columnWidths[0])
                    )
                    {
                        Text(
                            text = rank[0],
                            style = MaterialTheme.typography.headlineMedium,
                            color = if (index == 0) {
                                firstPlayerColor
                            } else {
                                Color.Unspecified
                            },
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                data.forEachIndexed { index, rank ->
                    Box(
                        modifier = modifier
                            .width(columnWidths[1])
                    ) {
                        Text(
                            text = rank[1],
                            style = MaterialTheme.typography.headlineMedium,
                            color = if (index == 0) {
                                firstPlayerColor
                            } else {
                                Color.Unspecified
                            },
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                }
            }
            Column {
                data.forEachIndexed { index, rank ->
                    Box(
                        modifier
                            .width(columnWidths[2])
                    ) {
                        Text(
                            text = rank[2],
                            style = MaterialTheme.typography.headlineMedium,
                            color = if (index == 0) {
                                firstPlayerColor
                            } else {
                                Color.Unspecified
                            },

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
