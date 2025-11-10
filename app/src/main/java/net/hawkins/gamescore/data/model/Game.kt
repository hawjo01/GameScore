package net.hawkins.gamescore.data.model

import net.hawkins.gamescore.data.model.game.Colors
import net.hawkins.gamescore.data.model.game.Rules

data class Game (val name: String,
                 val id: Int?,
                 val rules: Rules = Rules(),
                 val colors: Colors = Colors()
)