package com.example.roadfightercompose.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.roadfighter.R
import com.example.roadfightercompose.store.StoreDispatcher
import com.example.roadfightercompose.store.reducer.BlueCarActions

@Composable
fun BlueCar(
    id: Int,
    animation: Float,
    storeDispatcher: StoreDispatcher,
    height: Dp
) {
    val phaseShift = (id * 0.45f) % 1f
    var shiftedAnimation = animation + phaseShift
    if (shiftedAnimation > 1f) shiftedAnimation -= 1f

    var isVisible by remember { mutableStateOf(id == 0) }

    if (!isVisible && shiftedAnimation < 0.02f) {
        isVisible = true
    }

    val currentPosY = if (isVisible) height * shiftedAnimation else -height

    val blueCarPositionRange = 90..345
    val (blueCarOffset, setBlueCarOffset) = remember {
        mutableStateOf((blueCarPositionRange).random())
    }

    if ((0.000f..0.020f).contains(shiftedAnimation)) {
        setBlueCarOffset((blueCarPositionRange).random())
    }

    val offsetAnimation: Dp by animateDpAsState(blueCarOffset.dp)

    Image(
        painter = painterResource(R.drawable.blue_car),
        contentDescription = null,
        modifier = Modifier
            .absoluteOffset(
                x = offsetAnimation,
                y = currentPosY
            )
            .onGloballyPositioned { layoutCoordinates ->
                if (isVisible) {
                    storeDispatcher.dispatch(
                        BlueCarActions.UpdateBounds(id, layoutCoordinates.boundsInRoot())
                    )
                }
            },
        contentScale = ContentScale.Fit
    )
}