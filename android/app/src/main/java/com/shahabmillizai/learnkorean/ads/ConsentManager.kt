
package com.shahabmillizai.learnkorean.ads

import android.app.Activity
import android.util.Log
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

class ConsentManager(activity: Activity) {

    private val consentInformation =
        UserMessagingPlatform.getConsentInformation(activity)

    val canRequestAds: Boolean
        get() = consentInformation.canRequestAds()

    val isPrivacyOptionsRequired: Boolean
        get() = consentInformation.privacyOptionsRequirementStatus ==
            ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED

    fun gatherConsent(
        activity: Activity,
        onConsentGathered: () -> Unit
    ) {
        val parameters = ConsentRequestParameters.Builder().build()

        consentInformation.requestConsentInfoUpdate(
            activity,
            parameters,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { error ->
                    if (error != null) {
                        Log.w(AdConfig.TAG, error.message ?: "Consent form error")
                    }
                    onConsentGathered()
                }
            },
            { error ->
                Log.w(AdConfig.TAG, error.message ?: "Consent update error")
                onConsentGathered()
            }
        )
    }

    fun showPrivacyOptionsForm(
        activity: Activity,
        onComplete: () -> Unit
    ) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity) { error ->
            if (error != null) {
                Log.w(AdConfig.TAG, error.message ?: "Privacy options error")
            }
            onComplete()
        }
    }
}
