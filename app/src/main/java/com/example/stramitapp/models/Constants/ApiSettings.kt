package com.example.stramitapp.models.Constants

/**
 * Centrally manages API settings and environment configurations.
 */
object ApiSettings {

    // --- CHANGE THIS TO SWITCH ENVIRONMENTS ---
    private const val USE_PROD_SERVER = true

    // Server Hostnames
    private const val PROD_HOST = "54.206.135.82:8080"
    private const val TEST_HOST = "tst-astrack.stramit.com.au"

    // API Path Constants
    const val SCHEME = "http"
    const val ROOT = "ws"

    /**
     * The active host based on the environment flag.
     */
    val HOST: String = if (USE_PROD_SERVER) PROD_HOST else TEST_HOST

    /**
     * The base URL for the active environment.
     */
    val BASE_URL: String = "$SCHEME://$HOST"

    /**
     * The full URL including the root context (e.g., /ws/).
     */
    val FULL_BASE_URL: String = "$BASE_URL/$ROOT/"

    /**
     * Timeouts in seconds. 
     * PRODUCTION_DB_TIMEOUT is higher to accommodate large database downloads.
     */
    const val DEFAULT_TIMEOUT = 120L
    const val PRODUCTION_DB_TIMEOUT = 480L // 8 minutes for large production DB
}
