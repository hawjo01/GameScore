package net.hawkins.gamescore.data.source.impl

import net.hawkins.gamescore.data.source.GamePlayDataSource
import net.hawkins.gamescore.game.GamePlay
import java.io.File

class FileGamePlayDataSource(file: File) :
    JsonFileDataSource<GamePlay>(file, GamePlay::class.java),
    GamePlayDataSource