package com.example.roadfightercompose.store.reducer

import androidx.compose.ui.geometry.Rect
import mozilla.components.lib.state.Action

sealed class BlueCarActions : Action {
    data class UpdateBounds(
        val id: Int,
        val rect: Rect
    ) : BlueCarActions()

    object Reset : BlueCarActions()
}

val blueCarReducer = reducerFor<BlueCarActions> { state, action ->
    when (action) {
        is BlueCarActions.UpdateBounds -> {
            val newCars = state.blueCarState.cars.toMutableMap()
            newCars[action.id] = action.rect
            state.copy(blueCarState = state.blueCarState.copy(cars = newCars))
        }
        is BlueCarActions.Reset -> {
            state.copy(blueCarState = com.example.roadfightercompose.store.state.BlueCarState())
        }
    }
}