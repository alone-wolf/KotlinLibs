package top.writerpass.micromessage.client.pages.base

import androidx.navigation.NavArgument

interface PageArg : Page {
    val routeTemplate: String
    val args: List<String>
    val arguments: List<NavArgument>
}