package net.hawkins.gamescore.data.model

data class Leaderboard(
    val gameName: String = "",
    val rankings: List<Ranking> = emptyList(),
    val winner: String? = null
) {

    data class Ranking(
        val rank: Int,
        val score: Int,
        val playerNames: List<String>
    )
}

fun Leaderboard.toDataList(): List<List<String>> {
    val rankingsList = mutableListOf<List<String>>()
    rankings.forEach { ranking ->
        val rankData = listOf(
            ranking.rank.toString(),
            ranking.playerNames.joinToString(", "),
            ranking.score.toString()
        )
        rankingsList.add(rankData)
    }
    return rankingsList
}