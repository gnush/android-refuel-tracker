package gnush.refueltracker.ui.extensions

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import gnush.refueltracker.ui.Config
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Returns a String representation rounded to [Config.DECIMAL_PLACES_DEFAULT] decimal places.
 */
val BigDecimal.defaultText: String
    get() = this.setScale(Config.DECIMAL_PLACES_DEFAULT, RoundingMode.HALF_UP).toString()

/**
 * Returns a String representation rounded to [Config.DECIMAL_PLACES_SPECIAL] decimal places.
 */
val BigDecimal.specialText: String
    get() = this.setScale(Config.DECIMAL_PLACES_SPECIAL, RoundingMode.HALF_UP).toString()

/**
 * Returns '-' when the value is below zero
 *         '+' when the value is above zero
 *         the empty string when the value is zero
 */
val BigDecimal.displaySign: String
    get() = if (signum() < 0)
                "-"
            else if (signum() > 0)
                "+"
            else
                ""

/**
 * Returns colors from the current [MaterialTheme] when the the value above or below zero
 * *       [Color.Unspecified] when the value is zero
 */
val BigDecimal.valueChangeColor: Color
    @Composable get() = if (signum() < 0)
                            MaterialTheme.colorScheme.primary
                        else if (signum() > 0)
                            MaterialTheme.colorScheme.error
                        else
                            Color.Unspecified