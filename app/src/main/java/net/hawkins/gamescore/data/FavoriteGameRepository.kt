package net.hawkins.gamescore.data

import net.hawkins.gamescore.data.model.FavoriteGame
import net.hawkins.gamescore.data.source.IdableDataSource

class FavoriteGameRepository(dataSource: IdableDataSource<FavoriteGame>) :
    AbstractIdableRepository<FavoriteGame>(dataSource) {

    override fun getAll(): List<FavoriteGame> {
        val favoriteGames = super.getAll().toMutableList()
        // Filter-out games that don't have a valid GameType
        favoriteGames.retainAll { favoriteGame -> GameRepository.isGame(favoriteGame.game) }
        return favoriteGames
    }
}