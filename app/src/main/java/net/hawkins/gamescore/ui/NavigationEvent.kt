package net.hawkins.gamescore.ui

sealed class NavigationEvent {
    object NavigateToLeaderboard : NavigationEvent()
}