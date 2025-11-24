package top.writerpass.micromessage.client.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.ables.MutableStateComposeExt.CxOutlinedBasicTextField
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxTextButton
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.micromessage.client.LocalNavController
import top.writerpass.micromessage.client.pages.base.IPage

object LoginPage : IPage {
    override val routeBase: String
        get() = "login"
    override val label: String
        get() = "Login"
    override val leftTopIcon: @Composable (() -> Unit)
        get() = {}
    override val showTopAppBar: Boolean
        get() = false
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {
            FullSizeBox {
                val navController = LocalNavController.current

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val username = Mutable.someString("wolf")
                    val password = Mutable.someString("asdfghjkl")
                    "MicroMessage".CxText(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    username.CxOutlinedBasicTextField(
                        label = "Username"
                    )
                    password.CxOutlinedBasicTextField(
                        label = "Password",
                        visualTransformation = remember { PasswordVisualTransformation() }
                    )
                    "Login".CxTextButton {
                        navController.login()
                    }
                }
                Row(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp)) {
                    "Register".CxTextButton {
                        navController.open(RegisterPage)
                    }
                    "Reset Password".CxTextButton {
                        navController.open(ResetPasswordPage)
                    }
                }
            }
        }

}