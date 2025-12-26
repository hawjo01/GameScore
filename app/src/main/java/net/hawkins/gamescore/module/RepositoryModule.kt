package net.hawkins.gamescore.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.hawkins.gamescore.data.FavoriteGameRepository
import net.hawkins.gamescore.data.FavoritePlayerRepository
import net.hawkins.gamescore.data.GameProgressRepository
import net.hawkins.gamescore.data.GameRepository
import net.hawkins.gamescore.data.source.impl.FileFavoriteGameDataSource
import net.hawkins.gamescore.data.source.impl.FileFavoritePlayerDataSource
import net.hawkins.gamescore.data.source.impl.FileGameDataSource
import net.hawkins.gamescore.data.source.impl.FileGameProgressDataSource
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    const val FAVORITE_PLAYERS_FILENAME = "favorite-players.json"
    const val FAVORITE_GAMES_FILENAME = "favorite-games.json"
    const val GAMES_FILENAME = "games.json"
    const val GAME_PROGRESS_FILENAME = "game-progress.json"

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
    fun provideGameRepository(@ApplicationContext appContext: Context): GameRepository {
        val dataFile = File(appContext.filesDir, GAMES_FILENAME)
        if (!dataFile.exists()) {
            copyAssetFile(
                context = appContext,
                assetFileName = GAMES_FILENAME,
                destinationFile = dataFile
            )
        }

        return GameRepository(dataSource = FileGameDataSource(dataFile))
    }

    @Provides
    @Singleton
    fun provideGamePlayRepository(@ApplicationContext appContext: Context): GameProgressRepository {
        val dataFile = File(appContext.filesDir, GAME_PROGRESS_FILENAME)
        return GameProgressRepository(dataSource = FileGameProgressDataSource(dataFile))
    }

    private fun copyAssetFile(context: Context, assetFileName: String, destinationFile: File) {
        try {
            val assets = context.assets
            assets.open(assetFileName).use { inputStream ->
                FileOutputStream(destinationFile).use { outputStream ->
                    // Copy the content from the asset file to the destination file
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: IOException) {
            System.err.println("Failed to copy asset '" + assetFileName + "' to '" + destinationFile.absolutePath + "'")
            e.printStackTrace()
        }
    }
}