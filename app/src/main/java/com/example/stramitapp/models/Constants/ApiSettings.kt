package com.example.stramitapp.models.Constants

object ApiSettings {

    // --- CHANGE THIS TO SWITCH ENVIRONMENTS ---
    private const val USE_PROD_SERVER = false

    // Server Hostnames
    private const val PROD_HOST = "54.206.135.82:8080"
    private const val TEST_HOST = "tst-astrack.stramit.com.au"
    val SCHEME = if (USE_PROD_SERVER) {
        "http"
    } else {
        "https"
    }
    const val ROOT = "ws"
    val HOST: String = if (USE_PROD_SERVER) PROD_HOST else TEST_HOST
    val BASE_URL: String = "$SCHEME://$HOST"
    val FULL_BASE_URL: String = "$BASE_URL/$ROOT/"
    const val DEFAULT_TIMEOUT = 120L
    const val PRODUCTION_DB_TIMEOUT = 480L // 8 minutes for large production DB
}
