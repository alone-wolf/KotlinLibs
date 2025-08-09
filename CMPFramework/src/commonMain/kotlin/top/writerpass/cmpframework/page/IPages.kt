package top.writerpass.cmpframework.page

interface IPages {
    val pages: List<Page>
    val showBackButtonRoutes: List<String>
        get() = pages.filter { it.showBackButton }.map { it.route }
    val hideTopAppBarRoutes: List<String>
        get() = pages.filter { it.showTopAppBar }.map { it.route }
}
