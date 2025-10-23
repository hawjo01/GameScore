package net.hawkins.gamescore.data

import net.hawkins.gamescore.data.source.FavoritePlayerDataSource

class FavoritePlayerRepository(private val favoritePlayerDataSource: FavoritePlayerDataSource) {

    fun getPlayers(): List<String> {
        return favoritePlayerDataSource.getPlayers()
    }

    fun addPlayer(player: String) {
        favoritePlayerDataSource.savePlayer(player)
    }

    fun removePlayer(player: String) {
        favoritePlayerDataSource.deletePlayer(player)
    }
}