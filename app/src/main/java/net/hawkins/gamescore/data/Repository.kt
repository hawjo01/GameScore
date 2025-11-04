package net.hawkins.gamescore.data

import android.app.Application
import net.hawkins.gamescore.Constants.FAVORITE_GAMES_FILENAME
import net.hawkins.gamescore.Constants.FAVORITE_PLAYERS_FILENAME
import net.hawkins.gamescore.data.source.impl.FileFavoriteGameDataSource
import net.hawkins.gamescore.data.source.impl.FileFavoritePlayerDataSource
import java.io.File

interface Repository<T> {
    fun getAll(): List<T>
    fun save(item: T)
    fun delete(item: T)

    class Factory(application: Application) {

        private val directory: File = application.applicationContext.filesDir

        fun getFavoriteGameRepository(): FavoriteGameRepository {
            return FavoriteGameRepository(
                FileFavoriteGameDataSource(
                    File(
                        directory,
                        FAVORITE_GAMES_FILENAME
                    )
                )
            )
        }

        fun getFavoritePlayerRepository(): FavoritePlayerRepository {
            return FavoritePlayerRepository(
                FileFavoritePlayerDataSource(
                    File(
                        directory,
                        FAVORITE_PLAYERS_FILENAME
                    )
                )
            )
        }
    }
}