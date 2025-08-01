package top.writerpass.kotlinlibrary.shell.command

import java.io.File

fun testShellDsl() {
    println("=== Testing Shell DSL ===")
    
    val simpleCommand = ls("-la").run()
    println("Simple ls -la:")
    println("Exit code: ${simpleCommand.exitCode}")
    println("Output: ${simpleCommand.stdout.take(100)}...")
    
    val pipeCommand = echo("Hello DSL World!") * SingleCommand("tr", "a-z", "A-Z")
    val pipeResult = pipeCommand.run()
    println("\nPipe command: ${pipeCommand.build()}")
    println("Result: ${pipeResult.stdout.trim()}")
    
    val conditional = echo("Testing conditional") % echo("Success!")
    println("\nConditional: ${conditional.build()}")
    conditional.run()
    
    val complexExample = shell {
        val files = ls("-la")
        val filtered = files * grep(".kt")
        filtered.redirectTo(File("kotlin_files.txt"))
    }
    println("\nShell block result: $complexExample")
    
    val gitCheck = git("status", "--porcelain").run()
    if (gitCheck.isSuccess) {
        println("\nGit status: Clean")
    } else {
        println("\nGit status: Has changes")
    }
}

fun main() {
    testShellDsl()
}