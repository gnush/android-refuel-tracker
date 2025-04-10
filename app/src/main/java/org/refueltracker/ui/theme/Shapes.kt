package org.refueltracker.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = ShapeDefaults.ExtraSmall,
    small = ShapeDefaults.Small,
    medium = CutCornerShape(
        topEnd = 16.dp,
        bottomStart = 16.dp
    ),
    large = ShapeDefaults.Large,
    extraLarge = ShapeDefaults.ExtraLarge,
)