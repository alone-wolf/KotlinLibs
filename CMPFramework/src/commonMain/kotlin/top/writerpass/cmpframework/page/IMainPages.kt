package top.writerpass.cmpframework.page

interface IMainPages {
    val pages: List<MainPage>
    val routes: List<String>
        get() = pages.map { it.route }

    val showInBottomBarPages: List<MainPage>
        get() = pages.filter { it.showInBottomBar }

//    val showBackButtonRoutes: List<String>
//        get() = pages.filter { it.showBackButton }.map { it.route }
}

// BasePage
// OtherPage:BaePage
// MainPage:BasePage