package net.hawkins.gamescore.data.source.impl

import net.hawkins.gamescore.data.source.FavoritePlayerDataSource
import java.io.File

class FileFavoritePlayerDataSource(file: File) :
    JsonFileDataSource<String>(file, String::class.java),
    FavoritePlayerDataSource