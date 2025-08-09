package top.writerpass.cmpframework.builtin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import kotlinx.coroutines.launch
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
            "Login".Text()
            val username = Mutable.SomeString()
            val password = Mutable.SomeString()

            username.OutlinedTextFiled(label = "Username")
            password.OutlinedTextFiled(
                label = "Password",
                visualTransformation = PasswordVisualTransformation()
            )

            val loginManager = LocalLoginManager.current
            val scope = rememberCoroutineScope()
            "Login".OutlinedButton {
                scope.launch {
                    if (loginManager.check(username.value, password.value)) {
                        if (loginManager.login(username.value, password.value)) {
                            loginManager.leaveLoginPage()
                        }
                    }
                }
            }
            "Register".OutlinedButton {
                loginManager.gotoRegister()
            }
        }
    }
)

interface LoginManager {

    suspend fun check(username: String, password: String): Boolean
    suspend fun login(username: String, password: String): Boolean

    fun leaveLoginPage()

    fun gotoRegister()
}

val LocalLoginManager = staticCompositionLocalOf<LoginManager> {
    error("No LoginManager provided")
}

