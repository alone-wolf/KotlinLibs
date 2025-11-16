package top.writerpass.floatingwindowmanager

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText

class MyLifecycleOwner :
    LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner {

    fun onCreate() {
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    fun onResume() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    fun onPause() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }

    fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        store.clear()
    }

    /**
    Compose uses the Window's decor view to locate the
    Lifecycle/ViewModel/SavedStateRegistry owners.
    Therefore, we need to set this class as the "owner" for the decor view.
     */
    fun attachToDecorView(decorView: View?) {
        if (decorView == null) return
        decorView.setViewTreeLifecycleOwner(this)
        decorView.setViewTreeViewModelStoreOwner(this)
        decorView.setViewTreeSavedStateRegistryOwner(this)

//        ViewTreeLifecycleOwner.set(decorView, this)
//        ViewTreeViewModelStoreOwner.set(decorView, this)
//        ViewTreeSavedStateRegistryOwner.set(decorView, this)
    }

    // LifecycleOwner methods
    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    // ViewModelStore methods
    private val store = ViewModelStore()

    // SavedStateRegistry methods
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    override val lifecycle: Lifecycle = lifecycleRegistry
    override val viewModelStore: ViewModelStore = store
    override val savedStateRegistry: SavedStateRegistry =
        savedStateRegistryController.savedStateRegistry
}


class FloatingWindowService : Service(), ServiceForegroundable {
    override val foregroundNotificationChannelId: String = "floating_window_channel"

    @SuppressLint("NewApi")
    override val foregroundNotificationChannel: () -> NotificationChannel = {
        NotificationChannel(
            foregroundNotificationChannelId,
            "Floating Window Channel",
            NotificationManager.IMPORTANCE_MIN
        )
    }
    override val foregroundNotification: () -> Notification = {
        NotificationCompat.Builder(
            this,
            foregroundNotificationChannelId
        ).setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Floating Window")
            .setContentText("running...")
            .build()
    }
    override val foregroundNotificationId: Int = 100

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        "FloatingWindowService onStartCommand".toast()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun listLayoutParams(): LayoutParams {
        val layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        // 设置类型为悬浮窗
        layoutParams.type = LayoutParams.TYPE_APPLICATION_OVERLAY
        // 启用半透明支持
        layoutParams.format = PixelFormat.TRANSLUCENT

//        layoutParams.flags = LayoutParams.FLAG_ALT_FOCUSABLE_IM
        // 此窗口永远不会获得键盘输入焦点，因此用户无法向其发送键盘或其他按钮事件。
        // 这些事件将会发送到其后面的可获得焦点的窗口。
        // 无论是否明确设置，此标志还将启用FLAG_NOT_TOUCH_MODAL。
//    layoutParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE or
//            LayoutParams.FLAG_NOT_TOUCH_MODAL
        layoutParams.softInputMode = LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        layoutParams.flags =
            LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                    LayoutParams.FLAG_HARDWARE_ACCELERATED or
                    LayoutParams.FLAG_NOT_TOUCH_MODAL
        // 如果您已设置了FLAG_NOT_TOUCH_MODAL，您可以设置此标志以接收一个特殊的MotionEvent，
        // 其动作为MotionEvent.ACTION_OUTSIDE，用于处理窗口外发生的触摸事件。
        // 请注意，您将不会收到完整的按下/移动/抬起手势，只会收到第一个按下的位置作为ACTION_OUTSIDE。
//                LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        layoutParams.gravity = Gravity.START or Gravity.TOP
        layoutParams.x = 0
        layoutParams.y = 0
        return layoutParams
    }

    private fun aLP(): LayoutParams {
        return WindowManager.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            LayoutParams.TYPE_APPLICATION_OVERLAY,
            LayoutParams.FLAG_NOT_FOCUSABLE or
                    LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            softInputMode =
                LayoutParams.SOFT_INPUT_ADJUST_RESIZE or
                        LayoutParams.SOFT_INPUT_STATE_HIDDEN
        }
    }

    private val lifecycleOwner = MyLifecycleOwner()

    private val windowManager by lazy {
        getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private lateinit var view: View


    override fun onCreate() {
        super.onCreate()
        "FloatingWindowService onCreate".toast()
        startForegroundService()
        lifecycleOwner.onCreate()
        view = ComposeView(this).apply {
            lifecycleOwner.attachToDecorView(this)
            setContent {
                Box(
                    modifier = Modifier.wrapContentSize().border(1.dp, Color.Red)) {
                    Row(
                        modifier = Modifier
                            .width(250.dp)
                            .height(40.dp)
                            .clip(
                                RoundedCornerShape(8.dp, 0.dp, 0.dp, 8.dp)
                            )
                            .background(Color.DarkGray)
                    ) { }
                }
//                FullSizeBox(modifier = Modifier.border(1.dp, Color.Red)) {
//                    Row(
//                        modifier = Modifier
//                            .height(40.dp)
//                            .width(250.dp)
//                            .clip(
//                                RoundedCornerShape(8.dp, 0.dp, 0.dp, 8.dp)
//                            )
//                            .background(Color.DarkGray)
//                            .align(Alignment.TopEnd)
//                    ) { }
//                }
//                FullSizeBox(
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .background(Color.Red.copy(alpha = 0.5f))
//                ) {
//                    "Hello world!!!".CxText()
//                }
//                LazyColumn(modifier = Modifier.fillMaxSize()) {
//                    items(1000_000_000){it->
//                        "String ${it}".CxText(modifier = Modifier.padding(4.dp).height(14.dp))
//                    }
//                }
            }
        }
        windowManager.addView(view, aLP())
        lifecycleOwner.onResume()
    }

    override fun onDestroy() {
        stopForeground(Service.STOP_FOREGROUND_REMOVE)
        windowManager.removeView(view)
        super.onDestroy()
        "FloatingWindowService onDestroy".toast()
        lifecycleOwner.onPause()
        lifecycleOwner.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    fun String.toast() {
        Toast.makeText(
            this@FloatingWindowService,
            this,
            Toast.LENGTH_SHORT
        ).show()
    }

}


