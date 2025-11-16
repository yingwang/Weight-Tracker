package com.weighttracker.utils

object BMICalculator {
    /**
     * Calculate BMI (Body Mass Index)
     * @param weightKg Weight in kilograms
     * @param heightCm Height in centimeters
     * @return BMI value
     */
    fun calculate(weightKg: Float, heightCm: Float): Float {
        val heightM = heightCm / 100f
        return weightKg / (heightM * heightM)
    }

    /**
     * Get BMI category
     * @param bmi BMI value
     * @return BMI category string
     */
    fun getCategory(bmi: Float): String {
        return when {
            bmi < 18.5f -> "Underweight"
            bmi < 25f -> "Normal"
            bmi < 30f -> "Overweight"
            else -> "Obese"
        }
    }

    /**
     * Convert kg to lbs
     */
    fun kgToLbs(kg: Float): Float = kg * 2.20462f

    /**
     * Convert lbs to kg
     */
    fun lbsToKg(lbs: Float): Float = lbs / 2.20462f
}
