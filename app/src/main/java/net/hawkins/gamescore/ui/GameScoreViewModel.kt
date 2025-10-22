package net.hawkins.gamescore.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class TopAppBar(
    val title: String = "",
    val actions: @Composable (RowScope.() -> Unit)? = null
)

abstract class GameScoreViewModel : ViewModel() {
    @Suppress("unused")
    companion object {
        private val _topAppBar = mutableStateOf(TopAppBar())
    }

    val topAppBar: State<TopAppBar> = _topAppBar

    fun updateTopAppBar(newTitle: String = "", newActions: @Composable (RowScope.() -> Unit)?) {
        _topAppBar.value = TopAppBar(title = newTitle, actions = newActions)
    }
}