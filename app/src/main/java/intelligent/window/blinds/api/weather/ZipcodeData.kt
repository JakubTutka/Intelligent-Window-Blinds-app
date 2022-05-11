package intelligent.window.blinds.api.weather

data class ZipcodeData(
    val country: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    val zip: String
)