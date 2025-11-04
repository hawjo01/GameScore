package net.hawkins.gamescore.data

import net.hawkins.gamescore.data.source.DataSource

class FavoritePlayerRepository(dataSource: DataSource<String>) : AbstractRepository<String>(dataSource)