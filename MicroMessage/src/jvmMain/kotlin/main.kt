import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.window.application
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import top.writerpass.micromessage.client.ApplicationState
import top.writerpass.micromessage.client.LocalCurrentPage
import top.writerpass.micromessage.client.LocalNavController
import top.writerpass.micromessage.client.LocalSnackbarHostState
import top.writerpass.micromessage.client.rememberNavControllerWrapper
import top.writerpass.micromessage.client.rememberViewModelStoreOwner
import top.writerpass.micromessage.client.windows.AppMainWindow
import top.writerpass.micromessage.server.applicationServer

/**
 * 1. 登录/注册/重置密码 vv
 * 2. 主 Tab
 *    - 消息列表 v
 *    - 联系人 v
 *    - 发现 v
 *    - 我的 v
 * 3. 聊天界面
 *    - 私聊消息 v
 *    - 群聊消息
 *    - 私聊详情
 *    - 群聊详情
 * 5. 用户详情
 * 6. 添加好友
 * 7. 搜索页（全局/聊天内）
 * 8. 多媒体查看器
 * 9. 文件管理中心
 * 10. 通话（语音/视频）
 * 11. 账户与设置页
 * 12. 设备管理
 * 13. 消息收藏/归档
 * 14. 朋友圈/动态
 * 15. 系统通知中心
 * 16. 高级管理（支付、钱包等）
 */

@OptIn(ExperimentalMaterial3Api::class)
@DelicateCoroutinesApi
fun main() {
    GlobalScope.launch { applicationServer() }

    application {
        LaunchedEffect(ApplicationState.showMainWindow) {
            if (ApplicationState.showMainWindow.not()) {
                ::exitApplication.invoke()
            }
        }

        val snackbarHostState = remember { SnackbarHostState() }
        val navController = rememberNavControllerWrapper()
        val viewModelStoreOwner = rememberViewModelStoreOwner()
        val currentPage by navController.currentPageAsState()

        CompositionLocalProvider(
            values = arrayOf(
                LocalNavController provides navController,
                LocalViewModelStoreOwner provides viewModelStoreOwner,
                LocalSnackbarHostState provides snackbarHostState,
                LocalCurrentPage provides currentPage,
            ),
            content = {
                AppMainWindow()
            }
        )
    }
}



