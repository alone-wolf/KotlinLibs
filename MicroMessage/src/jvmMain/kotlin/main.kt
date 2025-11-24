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



