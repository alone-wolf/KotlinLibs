package top.writerpass.andlib

import android.app.Activity
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager

fun Activity.enterFullscreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        // Android 11+
        window.setDecorFitsSystemWindows(false)
        window.insetsController?.let { controller ->
            controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else {
        // Android 10 及以下
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        @Suppress("DEPRECATION")
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}


fun Activity.keepFullscreen() {
    val restoreDelay = 2000L // 恢复全屏的延迟时间（毫秒）
    val handler = Handler(Looper.getMainLooper())

    val applyFullscreen: () -> Unit = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    // 首次进入全屏
    applyFullscreen()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        // Android 11+
        window.decorView.setOnApplyWindowInsetsListener { v, insets ->
            val isVisible =
                insets.isVisible(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            if (isVisible) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({ applyFullscreen() }, restoreDelay)
            }
            v.onApplyWindowInsets(insets)
        }
    } else {
        // Android 10 及以下
        @Suppress("DEPRECATION")
        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            val isVisible = (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN) == 0
            if (isVisible) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({ applyFullscreen() }, restoreDelay)
            }
        }
    }
}
