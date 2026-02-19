package com.example.roadfightercompose.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roadfightercompose.store.StoreDispatcher
import com.example.roadfightercompose.store.reducer.GameStatusActions
import com.example.roadfightercompose.store.type.GameStatus

@Composable
fun StartScreen(
    storeDispatcher: StoreDispatcher? = null
) {
    // Animación de parpadeo para el botón
    val infiniteTransition = rememberInfiniteTransition(label = "Blink")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AlphaAnim"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF000022), Color.Black)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(80.dp)
        ) {
            // Título del juego
            Text(
                text = "ROAD\nFIGHTER",
                color = Color(0xFF00E5FF),
                fontSize = 64.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 6.sp,
                textAlign = TextAlign.Center,
                lineHeight = 64.sp,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color(0xFF00E5FF),
                        blurRadius = 30f
                    )
                )
            )

            // Botón de Inicio
            Box(
                modifier = Modifier
                    .clickable {
                        storeDispatcher?.dispatch(
                            GameStatusActions.Update(status = GameStatus.Running)
                        )
                    }
                    .background(
                        color = Color(0xFF1A1A1A),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 3.dp,
                        color = Color(0xFFFF0055),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 48.dp, vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "PRESS START",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.alpha(alpha)
                )
            }
        }
    }
}

@Preview
@Composable
fun StartScreenPreview() {
    StartScreen()
}