package net.hawkins.gamescore.data.source.impl

import net.hawkins.gamescore.data.model.GameProgress
import net.hawkins.gamescore.data.source.GameProgressDataSource
import java.io.File

class FileGameProgressDataSource(file: File) :
    IdableJsonFileDataSource<GameProgress>(file, GameProgress::class.java),
    GameProgressDataSource