package top.writerpass.animates.c

import androidx.navigation.NavHostController
import java.util.WeakHashMap

object FlipZoomControllerHolder {
    private val map = WeakHashMap<NavHostController, FlipZoomNavController>()
    fun getOrCreate(controller: NavHostController): FlipZoomNavController {
        return map.getOrPut(controller) { FlipZoomNavController(controller) }
    }
}