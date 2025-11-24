package top.writerpass.micromessage.client

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import top.writerpass.micromessage.client.pages.base.IPage
import top.writerpass.micromessage.client.pages.global.LoginPage
import top.writerpass.micromessage.client.pages.main.MessagePage

val LocalNavController = staticCompositionLocalOf<NavControllerWrapper> {
    error("No NavHostController provided")
}

val LocalCurrentPage = staticCompositionLocalOf<IPage> {
    error("No Page Provided")
}

@Composable
fun rememberViewModelStoreOwner(): ViewModelStoreOwner {
    return remember {
        object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore = ViewModelStore()
        }
    }
}

val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState Provided")
}


class NavControllerWrapper(
    private val navHostController: NavHostController
) {
    val c = navHostController
    fun open(page: IPage, vararg args: Any) {
        val newRoute = StringBuilder(page.routeBase).apply {
            args.forEach { it ->
                append("/")
                append(it)
            }
        }.toString()
        println("going to open:${newRoute}")
        navHostController.navigate(newRoute)
    }

    fun login() {
        navHostController.navigate(MessagePage.routeBase) {
            navHostController.popBackStack()
        }
    }

    fun logout() {
        navHostController.navigate(LoginPage.routeBase) {
            navHostController.popBackStack()
        }
    }

    @Composable
    fun currentPageAsState(): State<IPage> {
        val navBackStackEntry by c.currentBackStackEntryFlow.collectAsState(null)
        return remember {
            derivedStateOf {
                val currentRouteBase =
                    (navBackStackEntry?.destination?.route ?: LoginPage.routeBase)
                        .split("/").first()
                        .also { println("currentRouteBase: $it") }
                Pages.pageRouteMap[currentRouteBase]!!
            }
        }
    }

    fun popBackStack() = c.popBackStack()
}

@Composable
fun rememberNavControllerWrapper(): NavControllerWrapper {
    val c = rememberNavController()
    val d = remember {
        NavControllerWrapper(c)
    }
    return d
}