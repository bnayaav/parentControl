fun hotp(secretKey: String, counter: String): Long {
    // Your existing hotp implementation
    // Ensure the modulo operations are cast to Int
    // For example:

    // Example calculation
    val calculatedValue = someCalculation(secretKey, counter) // replace with your actual calculation
    return (calculatedValue % 10000).toLong()   // For generateChildCode
}

fun generateChildCode(secretKey: String, window: Int): Long {
    return hotp(secretKey, "CHILD:\$window") % 10000
}

fun generateUnlockCode(secretKey: String, window: Int): Long {
    return hotp(secretKey, "UNLOCK:\$window") % 1000000
}