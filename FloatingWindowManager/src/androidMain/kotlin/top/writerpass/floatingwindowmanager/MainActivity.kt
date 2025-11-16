package top.writerpass.floatingwindowmanager

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import top.writerpass.cmplibrary.LaunchedEffectOdd
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxTextButton


fun Context.hasPostNotificationPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
    } else {
        true // Android 12 及以下默认有权限
    }
}


class MainActivity : ComponentActivity() {
    private var notificationPermissionGranted by mutableStateOf(false)
    private var notificationPermissionDeniedByUser by mutableStateOf(false)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // 用户允许通知
            notificationPermissionGranted = true
        } else {
            notificationPermissionGranted = false
            notificationPermissionDeniedByUser = true
            // 被拒绝，可以提示用户去设置里开启
        }
    }

    private var floatingWindowPermissionGranted by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            Column(modifier = Modifier.systemBarsPadding()) {
                LaunchedEffectOdd {
                    notificationPermissionGranted = context.hasPostNotificationPermission()
                    floatingWindowPermissionGranted = Settings.canDrawOverlays(context)
                }
                if (notificationPermissionGranted.not()) {
                    "Request Notification Permission".CxTextButton {
                        if (notificationPermissionDeniedByUser.not()) {
                            requestPermissionLauncher.launch(
                                android.Manifest.permission.POST_NOTIFICATIONS
                            )
                        }
                    }
                }
                if (floatingWindowPermissionGranted.not()) {
                    "Check Floating Permission".CxTextButton {
                        val intent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:${context.packageName}")
                        )
                        startActivity(intent)
                    }
                }
                "Start Floating Service".CxTextButton {
                    ContextCompat.startForegroundService(
                        this@MainActivity,
                        Intent(this@MainActivity, FloatingWindowService::class.java)
                    )
                }
                "Stop Floating Service".CxTextButton {
                    context.stopService(
                        Intent(this@MainActivity, FloatingWindowService::class.java)
                    )
                }
            }
        }
    }
}