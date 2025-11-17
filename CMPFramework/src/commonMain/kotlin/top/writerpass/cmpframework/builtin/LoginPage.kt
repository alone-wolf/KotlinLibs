package top.writerpass.cmpframework.builtin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import kotlinx.coroutines.launch
import top.writerpass.cmpframework.page.Page
import top.writerpass.cmplibrary.compose.FullSizeColumn
import top.writerpass.cmplibrary.compose.ables.MutableStateComposeExt.CxOutlinedBasicTextField
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxOutlinedButton
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
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
            "Login".CxText()
            val username = Mutable.someString()
            val password = Mutable.someString()

            username.CxOutlinedBasicTextField(label = "Username")
            password.CxOutlinedBasicTextField(
                label = "Password",
                visualTransformation = PasswordVisualTransformation()
            )

            val loginManager = LocalLoginManager.current
            val scope = rememberCoroutineScope()
            "Login".CxOutlinedButton {
                scope.launch {
                    if (loginManager.check(username.value, password.value)) {
                        if (loginManager.login(username.value, password.value)) {
                            loginManager.leaveLoginPage()
                        }
                    }
                }
            }
            "Register".CxOutlinedButton {
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

