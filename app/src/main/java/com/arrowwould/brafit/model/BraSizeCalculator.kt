package com.arrowwould.brafit.model

import androidx.annotation.DrawableRes
import com.arrowwould.brafit.R

data class BraSizeResult(
    val bandSize: Int,
    val cupSize: String,
    val fullSize: String,
    val recommendedStyles: List<String>,
    @DrawableRes val braImageRes: Int,
    val description: String
)

object BraSizeCalculator {
    fun calculateBraSize(
        bustMeasurement: Double, 
        underBustMeasurement: Double
    ): BraSizeResult {
        // Input validation
        require(bustMeasurement > 0) { "Bust measurement must be positive" }
        require(underBustMeasurement > 0) { "Underbust measurement must be positive" }
        require(bustMeasurement >= underBustMeasurement) { "Bust must be larger than underbust" }

        // Round band size to nearest even number
        val bandSize = (Math.round(underBustMeasurement / 2.0) * 2).toInt()
        
        // Calculate cup size difference
        val difference = bustMeasurement - underBustMeasurement
        
        // Determine cup size and image based on difference
        val (cupSize, braImage, description) = when {
            difference < 1 -> Triple(
                "AA",
                R.drawable.bra_style_wireless,
                "Recommended: Wireless bras for smaller cup sizes"
            )
            difference < 2 -> Triple(
                "A",
                R.drawable.bra_style_light_padding,
                "Perfect for: Light padding and natural shaping"
            )
            difference < 3 -> Triple(
                "B",
                R.drawable.bra_style_tshirt,
                "Ideal for: T-shirt bras and everyday comfort"
            )
            difference < 4 -> Triple(
                "C",
                R.drawable.bra_style_full_coverage,
                "Best fit: Full coverage with medium support"
            )
            difference < 5 -> Triple(
                "D",
                R.drawable.bra_style_underwire,
                "Suggested: Underwire bras for added support"
            )
            difference < 6 -> Triple(
                "DD/E",
                R.drawable.bra_style_full_support,
                "Recommended: Full support with side panels"
            )
            difference < 7 -> Triple(
                "DDD/F",
                R.drawable.bra_style_minimizer,
                "Perfect for: Minimizer styles with maximum support"
            )
            difference < 8 -> Triple(
                "G",
                R.drawable.bra_style_structured,
                "Ideal for: Structured support with wider straps"
            )
            difference < 9 -> Triple(
                "H",
                R.drawable.bra_style_full_figure,
                "Best fit: Full figure styles with reinforced support"
            )
            difference < 10 -> Triple(
                "I",
                R.drawable.bra_style_maximum_support,
                "Suggested: Maximum support with comfort features"
            )
            else -> Triple(
                "J+",
                R.drawable.bra_style_specialty,
                "Recommended: Specialty sizes with custom support"
            )
        }

        // Create full size string
        val fullSize = "$bandSize$cupSize"

        // Recommend styles based on cup size
        val recommendedStyles = when {
            difference < 3 -> listOf(
                "Wireless Bras",
                "T-Shirt Bras",
                "Bralettes"
            )
            difference < 5 -> listOf(
                "Underwire Bras",
                "Full Coverage Bras",
                "Sports Bras"
            )
            else -> listOf(
                "Full Support Bras",
                "Minimizer Bras",
                "Side Support Bras"
            )
        }

        return BraSizeResult(
            bandSize = bandSize,
            cupSize = cupSize,
            fullSize = fullSize,
            recommendedStyles = recommendedStyles,
            braImageRes = braImage,
            description = description
        )
    }
} 