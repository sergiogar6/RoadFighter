package com.example.roadfightercompose.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
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
fun GameOver(
    score: Int,
    storeDispatcher: StoreDispatcher? = null
) {
    // Animación de parpadeo clásica de Arcade
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
                // Fondo con gradiente para darle profundidad en lugar de negro plano
                Brush.verticalGradient(
                    colors = listOf(Color(0xEE000000), Color(0xFF111111))
                )
            )
            .pointerInput(Unit) {
                detectTapGestures {
                    storeDispatcher?.dispatch(
                        GameStatusActions.Update(
                            status = GameStatus.Running
                        )
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            // game over titulo
            Text(
                text = "GAME OVER",
                color = Color(0xFFFF3B30),
                fontSize = 54.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Red,
                        blurRadius = 20f
                    )
                )
            )

            // Ppuntuacion final
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFF1A1A1A),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 3.dp,
                        color = Color(0xFFFFD700),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 48.dp, vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "FINAL SCORE",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = score.toString(),
                        color = Color(0xFF00E676),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Instrucción parpadeante para reiniciar
            Text(
                text = "TAP ANYWHERE TO RESTART",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.alpha(alpha), // Aplica la animación aquí
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun GameOverPreview() {
    GameOver(score = 3540)
}