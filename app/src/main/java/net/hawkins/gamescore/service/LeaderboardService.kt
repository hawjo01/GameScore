package net.hawkins.gamescore.service

import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.data.model.Leaderboard
import net.hawkins.gamescore.ui.gameplay.Player

class LeaderboardService {
    fun buildLeaderboard(game: Game, players: List<Player>): Leaderboard {
        val scoreGroups = players.groupBy { player -> player.totalScore() }
        val sortedScoreGroups =
            when (game.objective.type) {
                Game.Objective.Type.LOW_SCORE -> scoreGroups.toSortedMap()
                Game.Objective.Type.HIGH_SCORE -> scoreGroups.entries
                    .sortedByDescending { it.key }
                    .associate { it.toPair() }
            }

        val rankings = mutableListOf<Leaderboard.Ranking>()

        var nextRank = 1
        sortedScoreGroups.forEach { (score, players) ->
            val playerNames = players.map { player -> player.name }

            rankings.add(Leaderboard.Ranking(nextRank, score, playerNames))
            nextRank += players.size
        }

        return Leaderboard(gameName = game.name, rankings = rankings)
    }
}