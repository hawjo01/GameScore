package net.hawkins.gamescore.data.model

data class FavoriteGame(
    val name: String = "",
    val players: List<String> = emptyList(),
    val game: String = "",
    override var id: Int? = null
) : Idable