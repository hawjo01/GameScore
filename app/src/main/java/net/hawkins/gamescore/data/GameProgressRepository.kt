package net.hawkins.gamescore.data

import net.hawkins.gamescore.data.model.GameProgress
import net.hawkins.gamescore.data.source.IdableDataSource

class GameProgressRepository(dataSource: IdableDataSource<GameProgress>) :
    AbstractIdableRepository<GameProgress>(dataSource)