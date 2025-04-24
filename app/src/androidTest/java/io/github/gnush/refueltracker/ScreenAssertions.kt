package io.github.gnush.refueltracker

import androidx.navigation.NavController
import org.junit.Assert

fun NavController.assertCurrentRouteName(expected: String) {
    Assert.assertEquals(
        expected,
        currentBackStackEntry?.destination?.route
    )
}