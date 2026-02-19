package com.example.roadfightercompose.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.roadfightercompose.store.StoreDispatcher
import com.example.roadfightercompose.store.reducer.RoadActions
import com.example.roadfightercompose.store.state.RoadThemeAssets
import com.example.roadfightercompose.store.type.RoadSectionType

@Composable
fun BusyRoad(
    height: Dp,
    width: Dp,
    storeDispatcher: StoreDispatcher,
    animation: Float,
    roadColorFilter: Color,
    themeAssets: RoadThemeAssets
) {
    val roadSectionModifier = Modifier.size(width, height)
    val scrollState = rememberScrollState()
    val currentPosY = height * animation

    fun getDrawableForType(type: RoadSectionType): Int {
        return when (type) {
            RoadSectionType.Left -> themeAssets.left
            RoadSectionType.Center -> themeAssets.center
            RoadSectionType.Right -> themeAssets.right
        }
    }

    Row {
        Column(
            modifier = Modifier
                .roadSection(scrollState)
                .weight(weight = 1f, fill = false)
                .onGloballyPositioned { layoutCoordinates ->
                    storeDispatcher.dispatch(
                        RoadActions.UpdateLeftBounds(layoutCoordinates.boundsInRoot())
                    )
                }
        ) {
            RoadSection(
                drawableId = getDrawableForType(RoadSectionType.Left),
                modifier = roadSectionModifier,
                currentPosY = currentPosY,
                height = height,
                roadColorFilter = roadColorFilter
            )
        }

        Column(
            modifier = Modifier
                .roadSection(scrollState)
                .weight(weight = 2.5f, fill = true)
        ) {
            RoadSection(
                drawableId = getDrawableForType(RoadSectionType.Center),
                modifier = roadSectionModifier,
                currentPosY = currentPosY,
                height = height,
                roadColorFilter = roadColorFilter
            )
        }

        Column(
            modifier = Modifier
                .roadSection(scrollState)
                .weight(weight = 0.5f, fill = true)
                .onGloballyPositioned { layoutCoordinates ->
                    storeDispatcher.dispatch(
                        RoadActions.UpdateRightBounds(layoutCoordinates.boundsInRoot())
                    )
                }
        ) {
            RoadSection(
                drawableId = getDrawableForType(RoadSectionType.Right),
                modifier = roadSectionModifier,
                currentPosY = currentPosY,
                height = height,
                roadColorFilter = roadColorFilter
            )
        }
    }
}

@Composable
fun RoadSection(
    drawableId: Int,
    modifier: Modifier,
    currentPosY: Dp,
    height: Dp,
    roadColorFilter: Color
) {

    Box(modifier = modifier) {

        @Composable
        fun RoadTile(offsetY: Dp) {
            Crossfade(
                targetState = drawableId,
                animationSpec = tween(1000),
                label = "RoadAnim",
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = offsetY)
            ) { targetId ->
                val colorFilter = if (roadColorFilter != Color.Transparent) {
                    ColorFilter.tint(roadColorFilter, BlendMode.Color)
                } else {
                    null
                }

                Image(
                    painter = painterResource(targetId),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    colorFilter = colorFilter,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        RoadTile(offsetY = currentPosY)
        RoadTile(offsetY = currentPosY - height + 1.dp)
    }
}

private fun Modifier.roadSection(
    scrollState: ScrollState
): Modifier = this
    .then(verticalScroll(scrollState))
    .then(
        pointerInput(Unit) {
            detectVerticalDragGestures { change, _ ->
                change.consume()
            }
        }
    )