package net.hawkins.gamescore.ui

import android.app.Application
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

data class TopAppBar(
    val title: String = "",
    val actions: @Composable (RowScope.() -> Unit)? = null
)

abstract class AbstractViewModel(application: Application) : AndroidViewModel(application) {
    @Suppress("unused")
    companion object {
        private val _topAppBar = mutableStateOf(TopAppBar())
    }

    val topAppBar: State<TopAppBar> = _topAppBar

    fun updateTopAppBar(newTitle: String = "", newActions: @Composable (RowScope.() -> Unit)?) {
        _topAppBar.value = TopAppBar(title = newTitle, actions = newActions)
    }
}