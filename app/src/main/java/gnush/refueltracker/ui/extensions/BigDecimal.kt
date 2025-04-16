package gnush.refueltracker.ui.extensions

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.math.BigDecimal
import java.text.NumberFormat

/**
 * Returns a String representation conforming to the given [NumberFormat]
 */
fun BigDecimal.format(format: NumberFormat): String = format.format(this)

/**
 * Returns '- ' when the value is below zero
 *         '+ ' when the value is above zero
 *         the empty string when the value is zero
 */
val BigDecimal.paddedDisplaySign: String
    get() = if (signum() < 0)
                "- "
            else if (signum() > 0)
                "+ "
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