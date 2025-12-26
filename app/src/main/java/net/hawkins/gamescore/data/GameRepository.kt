package net.hawkins.gamescore.data

import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.data.source.IdableDataSource

class GameRepository(dataSource: IdableDataSource<Game>) :
    AbstractIdableRepository<Game>(dataSource)
