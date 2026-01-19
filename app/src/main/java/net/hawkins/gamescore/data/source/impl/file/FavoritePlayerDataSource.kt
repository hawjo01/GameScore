package net.hawkins.gamescore.data.source.impl.file

import net.hawkins.gamescore.data.source.FavoritePlayerDataSource
import java.io.File

class FavoritePlayerDataSource(file: File) :
    JsonDataSource<String>(file, String::class.java),
    FavoritePlayerDataSource