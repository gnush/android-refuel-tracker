package gnush.refueltracker.ui.extensions

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import gnush.refueltracker.ui.Config
import java.math.BigDecimal

/**
 * Returns a String representation conforming to [Config.VOLUME_FORMAT].
 */
val BigDecimal.volumeText: String
    get() = Config.VOLUME_FORMAT.format(this)

/**
 * Returns a String representation conforming to [Config.CURRENCY_FORMAT].
 */
val BigDecimal.currencyText: String
    get() = Config.CURRENCY_FORMAT.format(this)

/**
 * Returns a String representation conforming to [Config.CURRENCY_VOLUME_RATIO_FORMAT].
 */
val BigDecimal.ratioText: String
    get() = Config.CURRENCY_VOLUME_RATIO_FORMAT.format(this)

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