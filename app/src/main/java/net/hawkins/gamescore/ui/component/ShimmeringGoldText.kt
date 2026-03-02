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
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ShimmeringGoldText(
    text: String,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000, // slower = smoother
                easing = LinearEasing // smoother easing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    val goldColors = listOf(
        Color(0xFFF9A825), // Darker Gold
        Color(0xFFFFEB3B), // Bright Gold
        Color(0xFFF9A825)  // Darker Gold
    )
    val brush = Brush.linearGradient(
        colors = goldColors,
        start = Offset(translateAnim, translateAnim),
        end = Offset(translateAnim + 600f, translateAnim + 600f)
    )

    @OptIn(ExperimentalTextApi::class)
    Text(
        text = text,
        style = TextStyle(brush = brush, fontSize = 40.sp),
        fontWeight = FontWeight.ExtraBold,
        modifier = modifier
    )
}