package top.writerpass.cmpframework.page

interface IMainPages {
    val pages: List<MainPage>
    val routes: List<String>
        get() = pages.map { it.route }

    val pagesShow: List<MainPage>
        get() = pages.filter { it.hideInMore.not() }

    val hasMore: Boolean
        get() = pages.any { it.hideInMore }
    val pagesInMore: List<MainPage>
        get() = pages.filter { it.hideInMore }
}