package top.writerpass.rekuester

import top.writerpass.rekuester.viewmodel.ApiRepository

object Singletons {
    val apiRepository = ApiRepository()
    val client = RekuesterClient()
}