package net.hawkins.gamescore.ui.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel,
    onBack: () -> Unit,
    modifier: Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    viewModel.updateTopAppBar(
        newTitle = stringResource(R.string.leaderboard),
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
    LeaderboardTable(
        headers = listOf(
            stringResource(R.string.rank),
            stringResource(R.string.players),
            stringResource(R.string.score)
        ),
        data = uiState.leaderboard.toDataList(),
        modifier = modifier
    )
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
        columnWidth + 20.dp
    }

    return columnWidths
}

@Composable
private fun LeaderboardTable(
    headers: List<String>,
    data: List<List<String>>,
    modifier: Modifier
) {
    val columnWidths = findColumnWidths(headers = headers, data = data)
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        item {
            Row(
                modifier = modifier
                    .background(Color.Gray)
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                headers.forEachIndexed { index, header ->
                    val additionalModifier = when (index) {
                        0 -> Modifier.width(columnWidths[0])
                        1 -> Modifier.width(columnWidths[1])
                        else -> Modifier.weight(1f)
                    }

                    Text(
                        text = header,
                        modifier = Modifier
                            .padding(8.dp)
                            .then(additionalModifier),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        items(items = data) { row ->
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            ) {
                row.forEachIndexed { index, cell ->
                    val additionalModifier = when (index) {
                        0 -> Modifier.width(columnWidths[0])
                        1 -> Modifier.width(columnWidths[1])
                        else -> Modifier.weight(1f)
                    }

                    Text(
                        text = cell,
                        modifier = Modifier
                            .padding(8.dp)
                            .then(additionalModifier)
                    )
                }
            }
        }
    }
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
    val leaderboard = Leaderboard(gameName = gameName, rankings = rankings)
    val uiState = LeaderboardUiState(leaderboard)

    LeaderboardScreenContent(
        uiState = uiState,
        modifier = Modifier
    )
}
