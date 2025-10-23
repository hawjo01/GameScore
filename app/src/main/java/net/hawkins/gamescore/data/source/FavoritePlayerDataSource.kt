package net.hawkins.gamescore.data.source

interface FavoritePlayerDataSource {
    fun getPlayers(): List<String>
    fun savePlayer(player: String)
    fun deletePlayer(player: String)
}