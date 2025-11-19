package top.writerpass.micromessage.client

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import top.writerpass.micromessage.client.pages.base.IPage

//val LocalApplicationViewModel = staticCompositionLocalOf<ApplicationViewModel> {
//    error("No ApplicationViewModel provided")
//}
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
        navHostController.navigate(newRoute)
    }
}

@Composable
fun rememberNavControllerWrapper(): NavControllerWrapper {
    val c = rememberNavController()
    val d = remember {
        NavControllerWrapper(c)
    }
    return d
}