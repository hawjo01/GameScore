package net.hawkins.gamescore.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class TopAppBar(
    val title: String = "",
    val navigationIcon: @Composable (() -> Unit)? = null,
    val actions: @Composable (RowScope.() -> Unit)? = null
)

abstract class AbstractViewModel : ViewModel() {
    companion object {
        private val _topAppBar = mutableStateOf(TopAppBar())
    }

    val topAppBar: State<TopAppBar> = _topAppBar

    fun updateTopAppBar(
        newTitle: String = "",
        newNavigationIcon: @Composable (() -> Unit)? = null,
        newActions: @Composable (RowScope.() -> Unit)? = null
    ) {
        _topAppBar.value =
            TopAppBar(title = newTitle, navigationIcon = newNavigationIcon, actions = newActions)
    }
}