package com.pfc.inventorytrackerjpa.entities;

/**
 * Volume/weight measurement units with their milliliter equivalents.
 * Weight-based units (OUNCES, POUNDS, GRAMS, KILOGRAMS) use water-density
 * equivalents (1 ml = 1 g), which is a standard culinary approximation.
 */
public enum VolumeMeasurement {

    // --- Metric ---
    MILLILITERS(1.0),
    LITERS(1000.0),
    GRAMS(1.0),            // 1 ml of water ≈ 1 g
    KILOGRAMS(1000.0),     // 1 ml of water ≈ 1 g  →  1000 ml = 1 kg

    // --- Imperial volume ---
    TEASPOONS(4.92892),
    TABLESPOONS(14.7868),
    FLUID_OUNCES(29.5735),
    CUPS(236.588),
    PINTS(473.176),
    QUARTS(946.353),
    GALLONS(3785.41),

    // --- Imperial weight (water-density approximation) ---
    OUNCES(28.3495),       // 1 oz (weight) of water ≈ 28.35 ml
    POUNDS(453.592);       // 1 lb of water ≈ 453.59 ml

    /** How many milliliters are equal to 1 unit of this measurement. */
    private final double mlEquivalent;

    VolumeMeasurement(double mlEquivalent) {
        this.mlEquivalent = mlEquivalent;
    }

    public double getMlEquivalent() {
        return mlEquivalent;
    }
}
