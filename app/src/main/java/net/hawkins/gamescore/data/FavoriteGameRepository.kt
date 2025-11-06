package net.hawkins.gamescore.data

import net.hawkins.gamescore.data.source.DataSource
import net.hawkins.gamescore.game.type.Games
import net.hawkins.gamescore.data.model.FavoriteGame

class FavoriteGameRepository (dataSource : DataSource<FavoriteGame>) : AbstractRepository<FavoriteGame>(dataSource) {

    override fun getAll() : List<FavoriteGame> {
        val favoriteGames = super.getAll().toMutableList()
        // Filter-out games that don't have a valid GameType
        favoriteGames.retainAll { favoriteGame -> Games.isValidType( favoriteGame.game ) }
        return favoriteGames
    }
}