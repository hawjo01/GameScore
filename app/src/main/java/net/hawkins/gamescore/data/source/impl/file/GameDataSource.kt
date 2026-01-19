package net.hawkins.gamescore.data.source.impl.file

import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.data.source.GameDataSource
import java.io.File

class GameDataSource(file: File) :
    IdableJsonDataSource<Game>(file, Game::class.java),
    GameDataSource