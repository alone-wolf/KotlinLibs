package top.writerpass.micromessage.client.navigation.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIcon
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.micromessage.client.LocalNavController
import top.writerpass.micromessage.client.navigation.pages.base.IPage


object SearchPage : IPage {
    override val routeBase: String
        get() = "search"
    override val label: String
        get() = "搜索"
    override val leftTopIcon: @Composable () -> Unit
        get() = {
            val navController = LocalNavController.current
            Icons.Default.ArrowBackIosNew.CxIconButton {
                navController.popBackStack()
            }
        }
    override val actions: @Composable (RowScope.() -> Unit)
        get() = {}
    override val fab: @Composable (() -> Unit)
        get() = {}
    override val content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
        get() = { SearchContent() }
}

@Composable
fun SearchContent() {
    var searchQuery by remember { mutableStateOf("") }
    var showClearButton by remember { mutableStateOf(false) }

    // Sample search results data
    val recentSearches = listOf("张三", "李四", "王五", "产品经理", "Android开发")
    val searchHistory = listOf("项目讨论", "技术支持", "产品评审", "周报总结")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Search Bar
        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                showClearButton = it.isNotEmpty()
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .height(48.dp),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "搜索",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "搜索",
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }

                    if (showClearButton) {
                        Icons.Default.Clear.CxIconButton {
                            searchQuery = ""
                            showClearButton = false
                        }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (searchQuery.isEmpty()) {
            // Recent searches when no query
            LazyColumn {
                // Recent searches section
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        "最近搜索".CxText(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        "清空".CxText(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { }
                        )
                    }
                }

                items(recentSearches) { item ->
                    SearchSuggestionItem(
                        text = item,
                        onClick = { searchQuery = item }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Search history section
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        "搜索历史".CxText(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                items(searchHistory) { item ->
                    SearchSuggestionItem(
                        text = item,
                        onClick = { searchQuery = item }
                    )
                }
            }
        } else {
            // Search results when there's a query
            LazyColumn {
                items(listOf("联系人", "聊天记录", "文件", "公众号")) { category ->
                    SearchResultCategory(
                        category = category,
                        query = searchQuery,
                        onItemClick = { /* Handle item selection */ }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchSuggestionItem(
    text: String,
    onClick: () -> Unit
) {
    FullWidthRow(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icons.Default.Search.CxIcon(
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            text.CxText(fontSize = 15.sp)
        }
    }
}

@Composable
fun SearchResultCategory(
    category: String,
    query: String,
    onItemClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Category header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            category.CxText(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.weight(1f))
            "查看更多".CxText(
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { }
            )
        }

        // Sample result items
        SearchResultItem(
            title = "搜索结果的 $category",
            subtitle = "包含 \"$query\" 的相关内容",
            onClick = onItemClick
        )
        SearchResultItem(
            title = "另一个 $category 结果",
            subtitle = "匹配关键字: $query",
            onClick = onItemClick
        )
    }
}

@Composable
fun SearchResultItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    FullWidthRow(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            title.CxText(
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            subtitle.CxText(
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}