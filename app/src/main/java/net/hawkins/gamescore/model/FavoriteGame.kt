package net.hawkins.gamescore.model

data class FavoriteGame(
    val name: String = "",
    val players: List<String> = emptyList(),
    val game: String = ""
)