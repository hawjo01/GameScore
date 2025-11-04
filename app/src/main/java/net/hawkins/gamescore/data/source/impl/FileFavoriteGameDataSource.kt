package net.hawkins.gamescore.data.source.impl

import net.hawkins.gamescore.data.source.FavoriteGameDataSource
import net.hawkins.gamescore.model.FavoriteGame
import java.io.File

class FileFavoriteGameDataSource(file: File) :
    JsonFileDataSource<FavoriteGame>(file, FavoriteGame::class.java),
    FavoriteGameDataSource