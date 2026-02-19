package com.example.roadfightercompose.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.roadfightercompose.store.StoreDispatcher
import com.example.roadfightercompose.store.reducer.GameControlActions
import com.example.roadfightercompose.store.type.ControllerButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GameControls(
    modifier: Modifier = Modifier,
    storeDispatcher: StoreDispatcher
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 48.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ControlButton(
            imageVector = Icons.Default.KeyboardArrowLeft,
            baseColor = Color.Gray,
            onClick = {
                storeDispatcher.dispatch(
                    GameControlActions.OnClicked(ControllerButton.Left)
                )
            }
        )

        ControlButton(
            imageVector = Icons.Default.KeyboardArrowRight,
            baseColor = Color.Gray,
            onClick = {
                storeDispatcher.dispatch(
                    GameControlActions.OnClicked(ControllerButton.Right)
                )
            }
        )
    }
}

@Composable
fun ControlButton(
    imageVector: ImageVector,
    baseColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val buttonShape = RoundedCornerShape(24.dp)

    // 1. Estado para saber si se está tocando
    var isPressed by remember { mutableStateOf(false) }

    // 2. Animación de escala (1.0 = tamaño normal, 0.9 = hundido)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        label = "ButtonScale"
    )

    // 3. Si está presionado, se ilumina en blanco, si no, usa el color base (gris)
    val activeColor = if (isPressed) Color.White else baseColor

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .scale(scale) // Aplicamos la animación de hundirse
            .size(85.dp)
            .shadow(
                elevation = if (isPressed) 2.dp else 8.dp, // Pierde sombra al presionarse
                shape = buttonShape,
                ambientColor = activeColor,
                spotColor = activeColor
            )
            .clip(buttonShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        // Hacemos el fondo un poquito más claro al pulsar
                        if (isPressed) Color(0xDD333333) else Color(0xCC111111),
                        if (isPressed) Color(0xBB444444) else Color(0x99222222)
                    )
                )
            )
            .border(
                width = if (isPressed) 3.dp else 2.dp, // Borde más grueso al pulsar
                color = activeColor.copy(alpha = 0.8f),
                shape = buttonShape
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true // Indicamos que hemos empezado a pulsar
                        val job = scope.launch {
                            while(true) {
                                onClick()
                                delay(40)
                            }
                        }

                        tryAwaitRelease() // Esperamos hasta que el usuario levante el dedo

                        job.cancel()
                        isPressed = false // El dedo se ha levantado, vuelve a la normalidad
                    }
                )
            }
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = activeColor, // El icono también se vuelve blanco al pulsarlo
            modifier = Modifier.size(50.dp)
        )
    }
}

@Preview
@Composable
fun ControlButtonPreview() {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        ControlButton(
            imageVector = Icons.Default.KeyboardArrowLeft,
            baseColor = Color.Gray,
            onClick = {}
        )
        ControlButton(
            imageVector = Icons.Default.KeyboardArrowRight,
            baseColor = Color.Gray,
            onClick = {}
        )
    }
}