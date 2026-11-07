
package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.startapp.sdk.adsbase.StartAppSDK

class ConsentManager(context: Context) {

    private val appContext = context.applicationContext

    private val preferences = appContext.getSharedPreferences(
        "startio_privacy_preferences",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_CONSENT_SAVED = "consent_saved"
        private const val KEY_PERSONALIZED_ADS = "personalized_ads"
        private const val KEY_CONSENT_TIME = "consent_time"

        private const val PRIVACY_POLICY =
            "https://www.start.io/policy/privacy-policy/"
    }

    val canRequestAds: Boolean
        get() = preferences.getBoolean(KEY_CONSENT_SAVED, false)

    val isPrivacyOptionsRequired: Boolean
        get() = true

    private fun applyConsent(granted: Boolean) {
        val timestamp = preferences.getLong(
            KEY_CONSENT_TIME,
            System.currentTimeMillis()
        )

        try {
            StartAppSDK.setUserConsent(
                appContext,
                "pas",
                timestamp,
                granted
            )
        } catch (exception: Exception) {
            android.util.Log.e(
                "StartIoConsent",
                "Could not apply consent",
                exception
            )
        }
    }

    private fun saveConsent(granted: Boolean) {
        preferences.edit()
            .putBoolean(KEY_CONSENT_SAVED, true)
            .putBoolean(KEY_PERSONALIZED_ADS, granted)
            .putLong(KEY_CONSENT_TIME, System.currentTimeMillis())
            .apply()

        applyConsent(granted)

        android.util.Log.i(
            "StartIoConsent",
            "User consent saved: $granted"
        )
    }

    fun gatherConsent(
        activity: Activity,
        onConsentGathered: () -> Unit
    ) {
        if (preferences.getBoolean(KEY_CONSENT_SAVED, false)) {
            val granted = preferences.getBoolean(
                KEY_PERSONALIZED_ADS,
                false
            )

            applyConsent(granted)
            onConsentGathered()
            return
        }

        showConsentDialog(activity, onConsentGathered)
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

        val dialog = AlertDialog.Builder(activity)
            .setTitle("Your Privacy Choices")
            .setMessage(
                "Learn Korean uses Start.io advertising services.\n\n" +
                "You can allow or decline personalized advertising.\n\n" +
                "If you decline, advertisements may still appear, " +
                "but they may be less relevant.\n\n" +
                "Start.io Privacy Policy:\n$PRIVACY_POLICY"
            )
            .setPositiveButton("Allow") { _, _ ->
                finish(true)
            }
            .setNegativeButton("Decline") { _, _ ->
                finish(false)
            }
            .setNeutralButton("Privacy Policy") { _, _ ->
                try {
                    activity.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(PRIVACY_POLICY)
                        )
                    )
                } catch (exception: Exception) {
                    android.util.Log.e(
                        "StartIoConsent",
                        "Could not open privacy policy",
                        exception
                    )
                }
            }
            .setOnCancelListener {
                finish(false)
            }
            .create()

        dialog.setOnDismissListener {
            if (!completed) {
                finish(false)
            }
        }

        dialog.show()
    }

    fun showPrivacyOptionsForm(
        activity: Activity,
        onComplete: () -> Unit
    ) {
        showConsentDialog(activity, onComplete)
    }
}
