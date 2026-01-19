package net.hawkins.gamescore.data.source.impl.file

import net.hawkins.gamescore.data.model.GameProgress
import net.hawkins.gamescore.data.source.GameProgressDataSource
import java.io.File

class GameProgressDataSource(file: File) :
    IdableJsonDataSource<GameProgress>(file, GameProgress::class.java),
    GameProgressDataSource