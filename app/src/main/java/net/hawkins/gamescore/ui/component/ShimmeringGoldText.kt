package net.hawkins.gamescore.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ShimmeringGoldText(
    text: String,
    modifier: Modifier = Modifier
) {
    val goldColors = listOf(
        Color(0xFF8B4513), // Darker Gold/Bronze
        Color(0xFFFFD700), // Gold
        Color(0xFFFFECB3), // Lighter Gold
        Color(0xFFFFD700), // Gold
        Color(0xFF8B4513)  // Darker Gold/Bronze
    )

    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "translate"
    )

    val brush = Brush.linearGradient(
        colors = goldColors,
        start = Offset(translateAnim, translateAnim),
        end = Offset(translateAnim + 200f, translateAnim + 200f),
        tileMode = TileMode.Mirror
    )

    Text(
        text = text,
        style = TextStyle(
            brush = brush,
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        ),
        modifier = modifier
    )
}