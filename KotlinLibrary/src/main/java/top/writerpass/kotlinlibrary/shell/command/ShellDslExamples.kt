package top.writerpass.kotlinlibrary.shell.command

import java.io.File

object ShellDslExamples {

    fun basicExamples() {

        println("=== Basic Command Examples ===")

        val lsResult = ls("-la").run()
        println("LS Result: ${lsResult.stdout}")

        val pwdResult = pwd().run()
        println("PWD Result: ${pwdResult.stdout}")

        val echoResult = echo("Hello", "World").run()
        println("Echo Result: ${echoResult.stdout}")
    }

    fun chainingExamples() {

        println("=== Chaining Examples ===")

        val command1 = ls("-la") pipe grep(".kt")
        println("Pipe command: ${command1.build()}")

        val command2 = ls("-la").redirectTo(File("output.txt"))
        println("Redirect command: ${command2.build()}")

        val command3 = cat("file.txt").redirectFrom(File("input.txt"))
        println("Redirect from command: ${command3.build()}")
    }

    fun fluentDslExamples() {

        println("=== Fluent DSL Examples ===")

        val complexCommand = ls("-la")
            .pipe(grep(".kt"))
            .pipe(SingleCommand("wc", "-l"))
            .redirectTo(File("kotlin_file_count.txt"))

        println("Complex command: ${complexCommand.build()}")

        val conditional = ls("nonexistent")
            .or(echo("Directory doesn't exist"))
            .and(echo("Command succeeded"))

        println("Conditional command: ${conditional.build()}")
    }

    fun operatorOverloadExamples() {

        println("=== Operator Overload Examples ===")

        val pipeCommand = ls("-la") * grep(".kt")
        println("Pipe with operator: ${pipeCommand.build()}")

        val andCommand = ls("-la") % echo("Success")
        println("And with operator: ${andCommand.build()}")

        val orCommand = ls("-la") / echo("Failed")
        println("Or with operator: ${orCommand.build()}")

        val thenCommand = ls("-la") + echo("Done")
        println("Then with operator: ${thenCommand.build()}")
    }

    fun advancedExamples() {

        println("=== Advanced Examples ===")

        val backgroundTask = echo("Starting background task")
            .background()
            .logOutput("background.log")

        println("Background command: ${backgroundTask.build()}")

        val parallelCommands = ls("-la")
            .parallel(
                grep(".kt"),
                grep(".java")
            )

        println("Parallel commands: ${parallelCommands.build()}")

        val retryCommand = curl("https://example.com")
            .retry(3)
            .timeout(30)

        println("Retry command: ${retryCommand.build()}")
    }

    fun shellBlockExample() {

        println("=== Shell Block Example ===")

        val result = shell {
            val files = ls("-la").pipe(grep(".kt"))
            val count = files.pipe(SingleCommand("wc", "-l"))
            count.redirectTo(File("count.txt"))
        }

        println("Shell block result: $result")
    }

    fun gitWorkflowExample() {

        println("=== Git Workflow Example ===")

        val gitWorkflow = git("status")
            .pipe(grep("modified"))
            .ifSuccess {
                git("add", ".")
                    .then(git("commit", "-m", "Auto commit"))
                    .then(git("push"))
            }
            .ifFailure {
                echo("No changes to commit")
            }

        println("Git workflow: ${gitWorkflow.build()}")
    }

    fun fileProcessingExample() {

        println("=== File Processing Example ===")

        val processFiles = find(".", "-name", "*.log")
            .pipe(grep("ERROR"))
            .pipe(SingleCommand("sort"))
            .pipe(SingleCommand("uniq", "-c"))
            .pipe(SingleCommand("sort", "-nr"))
            .redirectTo(File("error_summary.txt"))
            .tee("error_summary_tee.txt")

        println("File processing: ${processFiles.build()}")
    }

    fun deploymentExample() {

        println("=== Deployment Example ===")

        val deployment = gradle("clean", "build")
            .withWorkingDirectory("/project/path")
            .ifSuccess {
                docker("build", "-t", "myapp:latest", ".")
                    .pipe(docker("run", "-d", "-p", "8080:8080", "myapp:latest"))
            }
            .ifFailure {
                echo("Build failed, check logs")
                    .logError("build_error.log")
            }

        println("Deployment: ${deployment.build()}")
    }

    fun monitoringExample() {

        println("=== Monitoring Example ===")

        val monitoring = curl("-s", "http://localhost:8080/health")
            .timeout(5)
            .retry(3)
            .ifFailure {
                echo("Service is down")
                    .pipe(SingleCommand("mail", "-s", "Service Down", "admin@company.com"))
            }
            .background()

        println("Monitoring: ${monitoring.build()}")
    }

    fun runAllExamples() {
        basicExamples()
        chainingExamples()
        fluentDslExamples()
        operatorOverloadExamples()
        advancedExamples()
        shellBlockExample()
        gitWorkflowExample()
        fileProcessingExample()
        deploymentExample()
        monitoringExample()
    }
}

fun main() {
    ShellDslExamples.runAllExamples()
}