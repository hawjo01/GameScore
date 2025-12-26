package net.hawkins.gamescore.data

import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.data.source.IdableDataSource

class GameRepository(dataSource: IdableDataSource<Game>):
    AbstractIdableRepository<Game>(dataSource) {

    // TODO: Temporary hack while implementing game ID's
    fun getByName(name: String): Game {
        return getAll().first {game -> game.name == name }
    }
}