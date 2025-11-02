package net.hawkins.gamescore.data.source

import net.hawkins.gamescore.game.GamePlay
import net.hawkins.gamescore.game.type.Games
import org.junit.Test
import java.io.File

class GameRulesDataSourceTest {

    @Test
    fun testMe() {
        val ds = FileGameRulesDataSource(File("C:\\temp\\rules.json"))

        Games.TYPES.forEach { type ->
            val gamePlay = type as GamePlay
            ds.saveGameRules(gamePlay.gameModel)
        }
    }
}