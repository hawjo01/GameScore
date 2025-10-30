package net.hawkins.gamescore.data.source

import java.io.File

class FileFavoritePlayerDataSource(val file: File) :
    JsonFileDataSource<String>(file, String::class.java),
    FavoritePlayerDataSource {

    override fun getPlayers(): List<String> {
        return getAll()
    }

    override fun savePlayer(player: String) {
        save(player)
    }

    override fun deletePlayer(player: String) {
        delete(player)
    }
}