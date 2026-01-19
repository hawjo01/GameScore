package net.hawkins.gamescore.data.source.impl.file

import net.hawkins.gamescore.data.model.FavoriteGame
import net.hawkins.gamescore.data.source.FavoriteGameDataSource
import java.io.File

class FavoriteGameDataSource(file: File) :
    IdableJsonDataSource<FavoriteGame>(file, FavoriteGame::class.java),
    FavoriteGameDataSource