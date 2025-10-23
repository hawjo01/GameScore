package net.hawkins.gamescore.data

import net.hawkins.gamescore.data.source.FavoriteGameDataSource
import net.hawkins.gamescore.game.Games
import net.hawkins.gamescore.model.FavoriteGame

class FavoriteGameRepository (private val favoriteGameDataSource : FavoriteGameDataSource) {

    fun getFavoriteGames() : List<FavoriteGame> {
        val favoriteGames = favoriteGameDataSource.getGames().toMutableList()
        // Filter-out games that don't have a GameType
        favoriteGames.retainAll { favoriteGame -> Games.isValidGame( favoriteGame.game ) }
        return favoriteGames
    }

    fun addFavoriteGame(favoriteGame: FavoriteGame) {
        favoriteGameDataSource.saveGame(favoriteGame)
    }

    fun removeFavoriteGame(favoriteGame: FavoriteGame) {
        favoriteGameDataSource.deleteGame(favoriteGame)
    }
}