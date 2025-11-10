package net.hawkins.gamescore.data.model.game

data class Colors(
    val negativeScore: Color = Color.DEFAULT,
    val positiveScore: Color = Color.DEFAULT
) {
    enum class Color {
        DEFAULT,
        RED
    }
}