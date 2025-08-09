package top.writerpass.cmpframework

import androidx.compose.runtime.staticCompositionLocalOf
import top.writerpass.cmpframework.navigation.NavControllerWrapper

val LocalNavControllerWrapper = staticCompositionLocalOf<NavControllerWrapper> {
    error("No NavHostControllerWrapper provided")
}