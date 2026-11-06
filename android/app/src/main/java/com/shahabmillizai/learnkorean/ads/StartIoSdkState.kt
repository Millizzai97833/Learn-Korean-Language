
package com.shahabmillizai.learnkorean.ads

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object StartIoSdkState {

    @Volatile
    private var sdkReady = false

    var isReady by mutableStateOf(false)
        private set

    private val lock = Any()
    private val pendingActions = mutableListOf<() -> Unit>()
    private val mainHandler = Handler(Looper.getMainLooper())

    fun isSdkReady(): Boolean = sdkReady

    fun runWhenReady(action: () -> Unit) {
        val runNow = synchronized(lock) {
            if (sdkReady) {
                true
            } else {
                pendingActions.add(action)
                false
            }
        }

        if (runNow) {
            mainHandler.post(action)
        }
    }

    fun markReady() {
        val actions = synchronized(lock) {
            sdkReady = true
            val copy = pendingActions.toList()
            pendingActions.clear()
            copy
        }

        mainHandler.post {
            isReady = true
            actions.forEach { action ->
                action()
            }
        }
    }
}
