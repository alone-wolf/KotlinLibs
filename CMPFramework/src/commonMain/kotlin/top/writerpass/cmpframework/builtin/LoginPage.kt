package top.writerpass.cmpframework.builtin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import top.writerpass.cmpframework.page.LocalNavController
import top.writerpass.cmpframework.page.Page
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.OutlinedButton
import top.writerpass.cmplibrary.compose.OutlinedTextFiled
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.utils.Mutable

internal val loginPage = Page(
    route = "login",
    label = "Login",
    showTopAppBar = false,
    content = {
        FullSizeColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            "Demo App Login".Text()
            val username = Mutable.SomeString("")
            val password = Mutable.SomeString("")

            username.OutlinedTextFiled(label = "Username")
            password.OutlinedTextFiled(
                label = "Password",
                visualTransformation = PasswordVisualTransformation()
            )

            val navController = LocalNavController.current
            "Login".OutlinedButton {
                navController.navigate("home")
            }
            "Register".OutlinedButton {
                navController.navigate("register")
            }
        }
    }
)

