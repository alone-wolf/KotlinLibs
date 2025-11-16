package top.writerpass.floatingwindowmanager

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.DefaultLifecycleObserver

class FloatingWindowManager(private val windowManager: WindowManager) : DefaultLifecycleObserver {
    private var floatingView: View? = null

    private val params by lazy {
        WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            // Android O+
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 200
            y = 500
        }
    }

    // --- Public API ---

    fun start(view: View) {
        floatingView = view

        windowManager.addView(floatingView, params)

        // 拖动逻辑（可选）
        enableDrag(floatingView!!)
    }

    fun stop() {
        floatingView?.let { windowManager.removeView(it) }
        floatingView = null
    }

    fun isShowing(): Boolean = floatingView != null

    /** 支持拖动移动悬浮窗 */
    @SuppressLint("ClickableViewAccessibility")
    private fun enableDrag(view: View) {
        var lastX = 0f
        var lastY = 0f
        var startX = 0
        var startY = 0

        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.rawX
                    lastY = event.rawY
                    startX = params.x
                    startY = params.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx = (event.rawX - lastX).toInt()
                    val dy = (event.rawY - lastY).toInt()
                    params.x = startX + dx
                    params.y = startY + dy
                    windowManager.updateViewLayout(view, params)
                }
            }
            true
        }
    }
}
