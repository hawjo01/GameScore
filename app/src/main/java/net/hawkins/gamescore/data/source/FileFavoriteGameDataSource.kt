package net.hawkins.gamescore.data.source

import net.hawkins.gamescore.model.FavoriteGame
import java.io.File

class FileFavoriteGameDataSource(val file: File) :
    JsonFileDataSource<FavoriteGame>(file, FavoriteGame::class.java),
    FavoriteGameDataSource {

    override fun getGames(): List<FavoriteGame> {
        return getAll()
    }

    override fun saveGame(favoriteGame: FavoriteGame) {
        save(favoriteGame)
    }

    override fun deleteGame(favoriteGame: FavoriteGame) {
        delete(favoriteGame)
    }
}