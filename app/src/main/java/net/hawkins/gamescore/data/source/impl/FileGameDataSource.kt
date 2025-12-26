package net.hawkins.gamescore.data.source.impl

import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.data.source.GameDataSource
import java.io.File

class FileGameDataSource(file: File) :
    IdableJsonFileDataSource<Game>(file, Game::class.java),
    GameDataSource