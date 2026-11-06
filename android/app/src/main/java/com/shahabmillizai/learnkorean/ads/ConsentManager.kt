package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.content.Context

/**
 * Compatibility wrapper while removing Google's UMP SDK.
 * Start.io manages its own consent prompt when required.
 */
class ConsentManager(private val context: Context) {

    fun gatherConsent(
        activity: Activity,
        onConsentGathered: () -> Unit
    ) {
        onConsentGathered()
    }

    val canRequestAds: Boolean
        get() = true

    val isPrivacyOptionsRequired: Boolean
        get() = false

    fun showPrivacyOptionsForm(
        activity: Activity,
        onComplete: () -> Unit
    ) {
        onComplete()
    }
}
