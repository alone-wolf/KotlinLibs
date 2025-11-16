package top.writerpass.floatingwindowmanager

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.compose.ui.platform.ComposeView
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import android.view.KeyEvent
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.AbstractComposeView
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

interface ServiceForegroundable {
    val foregroundNotificationChannelId: String
    val foregroundNotificationChannel: () -> NotificationChannel
    val foregroundNotification: () -> Notification
    val foregroundNotificationId: Int

    fun Service.startForegroundService() {
        val notificationChannel = foregroundNotificationChannel()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel)
        }

        startForeground(
            foregroundNotificationId,
            foregroundNotification()
        )
    }
}

//class MyLifecycleOwner : SavedStateRegistryOwner, ViewModelStoreOwner {
//    private val lifecycleRegistry = LifecycleRegistry(this)
//    private val savedStateRegistryController = SavedStateRegistryController.create(this)
//    override val viewModelStore = ViewModelStore()
//
//
//    fun handleLifecycleEvent(event: Lifecycle.Event) {
//        lifecycleRegistry.handleLifecycleEvent(event)
//    }
//
//    fun performRestore(bundle: Bundle?) {
//        savedStateRegistryController.performRestore(bundle)
//    }
//
//    override val savedStateRegistry: SavedStateRegistry =
//        savedStateRegistryController.savedStateRegistry
//    override val lifecycle: Lifecycle = lifecycleRegistry
//}

interface ComposeFloatable : ViewModelStoreOwner, SavedStateRegistryOwner {
    override val viewModelStore: ViewModelStore
    override val savedStateRegistry: SavedStateRegistry
    override val lifecycle: Lifecycle
}

abstract class BaseFloatingService : Service(), ServiceForegroundable {
//    ComposeFloatable

//    override val lifecycle: Lifecycle =
//    override val viewModelStore: ViewModelStore = ViewModelStore()
//    override val savedStateRegistry: SavedStateRegistry =
//        SavedStateRegistryController.create(this).savedStateRegistry

    @CallSuper
    override fun onCreate() {
        super.onCreate()
//        val klass = savedStateRegistry::class
//        klass.members.find { it.name == "performAttach" }?.call(savedStateRegistry, lifecycle)
//        klass.members.find { it.name == "performRestore" }?.call(savedStateRegistry, null)
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
    }

    @CallSuper
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    lateinit var windowManager: WindowManager

    fun displayView(view: ComposeView, layoutParams: WindowManager.LayoutParams) {
        windowManager.addView(view, layoutParams)
    }

    fun removeView(view: View) {
        windowManager.removeView(view)
    }

//    fun View.display(layoutParams: WindowManager.LayoutParams) {
//        displayView(this, layoutParams)
//    }
//
//    fun View.remove() {
//        windowManager.removeView(this)
//    }

    fun updateView(view: View, layoutParams: WindowManager.LayoutParams) {
        windowManager.updateViewLayout(view, layoutParams)
    }

    fun String.toast() {
        Toast.makeText(
            this@BaseFloatingService,
            this,
            Toast.LENGTH_SHORT
        ).show()
    }

}


//class ExtComposeView @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : AbstractComposeView(context, attrs, defStyleAttr) {
//
//    private val content = mutableStateOf<(@Composable () -> Unit)?>(null)
//
//    @Suppress("RedundantVisibilityModifier")
//    protected override var shouldCreateCompositionOnAttachedToWindow: Boolean = false
//        private set
//
//    @Composable
//    override fun Content() {
//        content.value?.invoke()
//    }
//
//    override fun getAccessibilityClassName(): CharSequence {
//        return javaClass.name
//    }
//
//    /**
//     * Set the Jetpack Compose UI content for this view.
//     * Initial composition will occur when the view becomes attached to a window or when
//     * [createComposition] is called, whichever comes first.
//     */
//    fun setContent(content: @Composable () -> Unit) {
//        shouldCreateCompositionOnAttachedToWindow = true
//        this.content.value = content
//        if (isAttachedToWindow) {
//            createComposition()
//        }
//    }
//
//    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
//        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
//            if (event.action == KeyEvent.ACTION_UP) {   //按键  按下和移开会有两个不同的事件所以需要区分
//                onBackPressed()
//            }
//        }
//        return super.dispatchKeyEvent(event)
//    }
//
//    var onBackPressed: () -> Unit = {}
//}