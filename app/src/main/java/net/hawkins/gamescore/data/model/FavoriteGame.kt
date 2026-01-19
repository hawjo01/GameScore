package net.hawkins.gamescore.data.model

data class FavoriteGame(
    val name: String = "",
    val players: List<String> = emptyList(),
    val game: Game,
    override var id: Int = 0
) : Idable