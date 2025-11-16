import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.automirrored.outlined.Launch
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.singleWindowApplication
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.client.LocalNavController

@Serializable
sealed interface MainPages {
    val icon: ImageVector
    val iconSelected: ImageVector
    val label: String
    val leftTopIcon: @Composable () -> Unit
    val actions: @Composable RowScope.() -> Unit

    @Serializable
    object Message : MainPages {
        override val icon: ImageVector
            get() = Icons.AutoMirrored.Outlined.Message
        override val iconSelected: ImageVector
            get() = Icons.AutoMirrored.Filled.Message
        override val label: String
            get() = "MicroMessage"
        override val leftTopIcon: @Composable (() -> Unit)
            get() = {}
        override val actions: @Composable (RowScope.() -> Unit)
            get() = {
                Icons.Default.AddCircleOutline.CxIconButton {

                }
            }
    }

    @Serializable
    object Contact : MainPages {
        override val icon: ImageVector
            get() = Icons.AutoMirrored.Outlined.List
        override val iconSelected: ImageVector
            get() = Icons.AutoMirrored.Filled.List
        override val label: String
            get() = "Contact"
        override val leftTopIcon: @Composable (() -> Unit)
            get() = {}
        override val actions: @Composable (RowScope.() -> Unit)
            get() = {
                Icons.Default.AddCircle.CxIconButton { }
            }
    }

    @Serializable
    object Explorer : MainPages {
        override val icon: ImageVector
            get() = Icons.AutoMirrored.Outlined.Article
        override val iconSelected: ImageVector
            get() = Icons.AutoMirrored.Filled.Article
        override val label: String
            get() = "Explorer"
        override val leftTopIcon: @Composable (() -> Unit)
            get() = {}
        override val actions: @Composable (RowScope.() -> Unit)
            get() = {}

    }

    @Serializable
    object Me : MainPages {
        override val icon: ImageVector
            get() = Icons.AutoMirrored.Outlined.Launch
        override val iconSelected: ImageVector
            get() = Icons.AutoMirrored.Filled.Launch
        override val label: String
            get() = "Me"
        override val leftTopIcon: @Composable (() -> Unit)
            get() = {}
        override val actions: @Composable (RowScope.() -> Unit)
            get() = {}
    }

    companion object {
        val pages = listOf<MainPages>(
            MainPages.Message,
            MainPages.Contact,
            MainPages.Explorer,
            MainPages.Me
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAvatarPage() {
    val navController = LocalNavController.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("个人头像") },
                navigationIcon = {
                    Icons.Default.ArrowBackIosNew.CxIconButton {
                        navController.popBackStack()
                    }
                },
                actions = { Icons.Default.MoreHoriz.CxIconButton { } },
            )
        },
        snackbarHost = {},
        content = {
            FullSizeBox {
                Icons.Default.Person2.CxIcon(
                    modifier = Modifier.fillMaxSize()
                )
            }
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalProfilePage() {
    val navController = LocalNavController.current

    fun LazyListScope.infoItem(
        label: String,
        extra: @Composable () -> Unit,
        onClick: () -> Unit = {}
    ) {
        item {
            FullWidthRow(
                modifier = Modifier
                    .clickable(onClick = onClick)
                    .padding(vertical = 18.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    label.CxText(fontSize = 16.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    extra()
                    Icons.Default.ArrowRight.CxIcon()
                }
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("个人资料") },
                navigationIcon = {
                    Icons.Default.ArrowBackIosNew.CxIconButton {
                        navController.popBackStack()
                    }
                },
            )
        },
        content = {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                infoItem(
                    label = "头像",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "名字",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "性别",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "地区",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "手机号",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "微信号",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "我的二维码",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "拍一拍",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "签名",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "来电铃声",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "我的地址",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "我的发票抬头",
                    extra = {},
                    onClick = {

                    }
                )
                infoItem(
                    label = "微信豆",
                    extra = {},
                    onClick = {

                    }
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
fun main() = singleWindowApplication {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentRouteObj by remember {
        derivedStateOf {
            when (currentRoute) {
                MainPages.Message::class.qualifiedName -> {
                    MainPages.Message
                }

                MainPages.Contact::class.qualifiedName -> {
                    MainPages.Contact
                }

                MainPages.Explorer::class.qualifiedName -> {
                    MainPages.Explorer
                }

                MainPages.Me::class.qualifiedName -> {
                    MainPages.Me
                }

                else -> MainPages.Message
            }
        }
    }
    CompositionLocalProvider(
        LocalNavController provides navController
    ) {
        val navController = LocalNavController.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { currentRouteObj.label.CxText() },
                    navigationIcon = { currentRouteObj.leftTopIcon() },
                    actions = { currentRouteObj.actions(this) }
                )
            },
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar {
                    MainPages.pages.forEach { page ->
                        NavigationBarItem(
                            selected = false,
                            icon = {
                                page.icon.CxIcon()
                            },
                            onClick = {
                                navController.navigate()
                            }
                        )
                    }

                }
            },
            snackbarHost = {
                val snackbarHostState = remember { SnackbarHostState() }
                SnackbarHost(
                    hostState = snackbarHostState,
                    snackbar = {
//                        snackbarHostState.
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {},
                    content = {
                        Icons.Default.Search.CxIcon()
                    }
                )
            },
            floatingActionButtonPosition = FabPosition.EndOverlay,
            content = { padding ->
                NavHost(
                    navController = navController,
                    startDestination = MainPages.Message,
                    modifier = Modifier.padding(padding)
                ) {
                    composable<MainPages.Message> {

                    }

                    composable<MainPages.Contact> { }

                    composable<MainPages.Explorer> { }

                    composable<MainPages.Me> { }
                }
            },
        )

    }

}