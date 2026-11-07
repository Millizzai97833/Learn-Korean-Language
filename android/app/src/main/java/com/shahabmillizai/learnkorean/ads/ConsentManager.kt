
package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.startapp.sdk.adsbase.StartAppSDK

class ConsentManager(context: Context) {

    private val appContext = context.applicationContext

    private val preferences = appContext.getSharedPreferences(
        "startio_privacy_preferences",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val TAG = "StartIoConsent"
        private const val KEY_SAVED = "consent_saved"
        private const val KEY_PERSONALIZED = "personalized_ads"
        private const val KEY_TIMESTAMP = "consent_timestamp"
    }

    val canRequestAds: Boolean
        get() = preferences.getBoolean(KEY_SAVED, false)

    val isPrivacyOptionsRequired: Boolean
        get() = true

    private fun applyConsent(granted: Boolean) {
        val timestamp = preferences.getLong(
            KEY_TIMESTAMP,
            System.currentTimeMillis()
        )

        try {
            StartAppSDK.setUserConsent(
                appContext,
                "pas",
                timestamp,
                granted
            )

            Log.i(TAG, "Consent submitted: $granted")
        } catch (e: Exception) {
            Log.e(TAG, "Could not submit consent", e)
        }
    }

    private fun saveConsent(granted: Boolean) {
        preferences.edit()
            .putBoolean(KEY_SAVED, true)
            .putBoolean(KEY_PERSONALIZED, granted)
            .putLong(KEY_TIMESTAMP, System.currentTimeMillis())
            .apply()

        applyConsent(granted)
    }

    fun gatherConsent(
        activity: Activity,
        onConsentGathered: () -> Unit
    ) {
        if (preferences.getBoolean(KEY_SAVED, false)) {
            val granted = preferences.getBoolean(
                KEY_PERSONALIZED,
                false
            )

            applyConsent(granted)
            onConsentGathered()
            return
        }

        showConsentDialog(activity, onConsentGathered)
    }

    fun showPrivacyOptionsForm(
        activity: Activity,
        onComplete: () -> Unit
    ) {
        showConsentDialog(activity, onComplete)
    }

    private fun showConsentDialog(
        activity: Activity,
        onComplete: () -> Unit
    ) {
        if (activity.isFinishing || activity.isDestroyed) {
            return
        }

        var completed = false

        fun finish(granted: Boolean) {
            if (completed) return
            completed = true

            saveConsent(granted)
            onComplete()
        }

        AlertDialog.Builder(activity)
            .setTitle("Advertising Privacy Choices")
            .setMessage(
                "Learn Korean Language uses Start.io advertising.\n\n" +
                "Start.io may process information such as device " +
                "identifiers, IP address, device information and " +
                "other information described in its privacy policy.\n\n" +
                "Choose whether to allow personalized advertising. " +
                "If you decline, non-personalized or contextual ads " +
                "may still be available.\n\n" +
                "Privacy policy:\n" +
                "https://www.start.io/policy/privacy-policy/"
            )
            .setPositiveButton("Allow Personalized Ads") { _, _ ->
                finish(true)
            }
            .setNegativeButton("Continue Without Personalized Ads") { _, _ ->
                finish(false)
            }
            .setOnCancelListener {
                finish(false)
            }
            .show()
    }
}
