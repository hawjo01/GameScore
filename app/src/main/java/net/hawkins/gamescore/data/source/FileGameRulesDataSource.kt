package net.hawkins.gamescore.data.source

import net.hawkins.gamescore.game.GameModel
import java.io.File

class FileGameRulesDataSource(val file: File) :
JsonFileDataSource<GameModel>(file, GameModel::class.java)
{
    fun getGameRules(): List<GameModel> {
        return getAll()
    }
    fun saveGameRules(gameModel: GameModel) {
        save(gameModel)
    }
    fun deleteGameRules(gameModel: GameModel) {
        delete(gameModel)
    }
}