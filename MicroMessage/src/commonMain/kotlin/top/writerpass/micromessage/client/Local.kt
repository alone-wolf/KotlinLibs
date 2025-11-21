package top.writerpass.micromessage.client

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import top.writerpass.micromessage.client.pages.base.IPage

val LocalNavController = staticCompositionLocalOf<NavControllerWrapper> {
    error("No NavHostController provided")
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

    @Composable
    fun currentBackStackEntryAsState(): State<NavBackStackEntry?> {
        return c.currentBackStackEntryFlow.collectAsState(null)
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