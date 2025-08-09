package top.writerpass.cmpframework.page

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import top.writerpass.cmpframework.navigation.NavControllerWrapper

val LocalNavControllerWrapper = staticCompositionLocalOf<NavControllerWrapper> {
    error("No NavHostControllerWrapper provided")
}