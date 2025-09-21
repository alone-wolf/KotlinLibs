package top.writerpass.resloader

fun main() {
    val text = ResourceLoader.readText("files/sample.txt")
    println(text)
}