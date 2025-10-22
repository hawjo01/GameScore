package net.hawkins.gamescore.data.source

import net.hawkins.gamescore.model.FavoriteGame

interface FavoriteGameDataSource {
    fun getGames(): List<FavoriteGame>
    fun saveGame(favoriteGame: FavoriteGame)
    fun deleteGame(favoriteGame: FavoriteGame)
}