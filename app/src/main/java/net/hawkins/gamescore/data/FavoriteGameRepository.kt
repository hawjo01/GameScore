package net.hawkins.gamescore.data

import net.hawkins.gamescore.data.model.FavoriteGame
import net.hawkins.gamescore.data.source.IdableDataSource

class FavoriteGameRepository(dataSource: IdableDataSource<FavoriteGame>) :
    AbstractIdableRepository<FavoriteGame>(dataSource)