package net.hawkins.gamescore.data

import net.hawkins.gamescore.data.source.DataSource
import net.hawkins.gamescore.game.GamePlay

class GamePlayRepository(dataSource: DataSource<GamePlay>) : AbstractRepository<GamePlay>(dataSource)