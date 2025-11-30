package top.writerpass.micromessage.client.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import top.writerpass.micromessage.client.NavControllerWrapper
import top.writerpass.micromessage.client.navigation.pages.base.IPage

@Composable
fun AppMainWindowNavHost(
    navControllerWrapper: NavControllerWrapper,
    startDestination: IPage,
    modifier: Modifier,
    pages: List<IPage>
) {
    NavHost(
        navController = navControllerWrapper.c,
        startDestination = startDestination.routeBase,
        modifier = modifier,
    ) {
        pages.forEach { page ->
            noAnimeComposable(
                routeTemplate = page.routeTemplate,
                arguments = page.arguments,
                deepLinks = page.deepLink,
                content = page.content
            )
        }
    }
}

fun NavGraphBuilder.noAnimeComposable(
    routeTemplate: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) = composable(
    route = routeTemplate,
    arguments = arguments,
    deepLinks = deepLinks,
    enterTransition = { EnterTransition.None },
    exitTransition = { ExitTransition.None },
    popEnterTransition = { EnterTransition.None },
    popExitTransition = { ExitTransition.None },
    content = content
)