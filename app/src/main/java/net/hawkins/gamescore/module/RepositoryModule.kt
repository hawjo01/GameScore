package net.hawkins.gamescore.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.hawkins.gamescore.data.FavoriteGameRepository
import net.hawkins.gamescore.data.FavoritePlayerRepository
import net.hawkins.gamescore.data.GameRepository
import net.hawkins.gamescore.data.source.impl.FileFavoriteGameDataSource
import net.hawkins.gamescore.data.source.impl.FileFavoritePlayerDataSource
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    const val FAVORITE_PLAYERS_FILENAME = "favorite-players.json"
    const val FAVORITE_GAMES_FILENAME = "favorite-games.json"

    @Provides
    @Singleton
    fun provideFavoriteGameRepository(@ApplicationContext appContext: Context): FavoriteGameRepository {
        val directory: File = appContext.filesDir

        return FavoriteGameRepository(
            dataSource = FileFavoriteGameDataSource(
                File(
                    directory,
                    FAVORITE_GAMES_FILENAME
                )
            )
        )
    }

    @Provides
    @Singleton
    fun provideFavoritePlayerRepository(@ApplicationContext appContext: Context): FavoritePlayerRepository {
        val directory: File = appContext.filesDir
        return FavoritePlayerRepository(
            FileFavoritePlayerDataSource(
                File(
                    directory,
                    FAVORITE_PLAYERS_FILENAME
                )
            )
        )
    }

    @Provides
    @Singleton
    fun provideGameRepository(): GameRepository {
        return GameRepository()
    }
}