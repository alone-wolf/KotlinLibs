package top.writerpass.micromessage.client

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

//val LocalApplicationViewModel = staticCompositionLocalOf<ApplicationViewModel> {
//    error("No ApplicationViewModel provided")
//}
val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavHostController provided")
}