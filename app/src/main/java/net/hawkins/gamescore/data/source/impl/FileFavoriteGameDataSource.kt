package net.hawkins.gamescore.data.source.impl

import net.hawkins.gamescore.data.source.FavoriteGameDataSource
import net.hawkins.gamescore.data.model.FavoriteGame
import java.io.File

class FileFavoriteGameDataSource(file: File) :
    IdableJsonFileDataSource<FavoriteGame>(file, FavoriteGame::class.java),
    FavoriteGameDataSource