package com.shahabmillizai.learnkorean.ads

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Thread-safe lifecycle state tracker for Start.io SDK.
 * Banners and Rewarded ads observe this state before initiating any ad requests.
 */
object StartIoSdkState {

    @Volatile
    private var sdkReady = false

    @Volatile
    private var sdkFailed = false

    var isReady by mutableStateOf(false)
        private set

    var isFailed by mutableStateOf(false)
        private set

    var failureReason by mutableStateOf<String?>(null)
        private set

    private val lock = Any()
    private val pendingActions = mutableListOf<() -> Unit>()
    private val mainHandler = Handler(Looper.getMainLooper())

    fun isSdkReady(): Boolean = sdkReady

    fun isSdkFailed(): Boolean = sdkFailed

    /**
     * Executes the given action once the SDK has successfully initialized.
     * If the SDK is already ready, executes immediately on the main thread.
     * If the SDK failed to initialize, the action is discarded safely.
     */
    fun runWhenReady(action: () -> Unit) {
        val (shouldRunNow, isAlreadyFailed) = synchronized(lock) {
            when {
                sdkReady -> Pair(true, false)
                sdkFailed -> Pair(false, true)
                else -> {
                    pendingActions.add(action)
                    Pair(false, false)
                }
            }
        }

        if (shouldRunNow) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                action()
            } else {
                mainHandler.post(action)
            }
        } else if (isAlreadyFailed) {
            Log.w(
                AdConfig.TAG_SDK,
                "Action skipped because Start.io SDK initialization failed earlier"
            )
        }
    }

    /**
     * Called ONLY from the StartAppSDK initialization callback upon verified success.
     */
    fun markReady() {
        val actionsToRun = synchronized(lock) {
            sdkReady = true
            sdkFailed = false
            failureReason = null
            val copy = pendingActions.toList()
            pendingActions.clear()
            copy
        }

        Log.i(
            AdConfig.TAG_SDK,
            "Start.io SDK confirmed ready. Dispatching ${actionsToRun.size} queued ad request(s)."
        )

        mainHandler.post {
            isReady = true
            isFailed = false
            actionsToRun.forEach { action ->
                try {
                    action()
                } catch (e: Exception) {
                    Log.e(AdConfig.TAG_SDK, "Error executing pending ad action", e)
                }
            }
        }
    }

    /**
     * Called when StartAppSDK initialization fails. Never marks the SDK as ready.
     */
    fun markFailed(reason: String) {
        synchronized(lock) {
            sdkReady = false
            sdkFailed = true
            failureReason = reason
            val droppedCount = pendingActions.size
            pendingActions.clear()
            if (droppedCount > 0) {
                Log.w(
                    AdConfig.TAG_SDK,
                    "Start.io SDK initialization failed ($reason). Dropped $droppedCount pending ad action(s)."
                )
            }
        }

        mainHandler.post {
            isReady = false
            isFailed = true
        }
    }
}
