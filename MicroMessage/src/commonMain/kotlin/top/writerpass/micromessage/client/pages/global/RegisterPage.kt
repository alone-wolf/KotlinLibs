package top.writerpass.micromessage.client.pages.global

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.ables.IconComposeExt.CxIconButton
import top.writerpass.cmplibrary.compose.ables.MutableStateComposeExt.CxOutlinedBasicTextField
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxText
import top.writerpass.cmplibrary.compose.ables.TextComposeExt.CxTextButton
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.micromessage.client.LocalNavController
import top.writerpass.micromessage.client.pages.base.IPage

object RegisterPage : IPage {
    override val routeBase: String
        get() = "register"
    override val label: String
        get() = "Register"
    override val leftTopIcon: @Composable (() -> Unit)
        get() = {}
    override val showTopAppBar: Boolean
        get() = false
    override val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
        get() = {
            FullSizeBox {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val navController = LocalNavController.current
                    val username = Mutable.someString("wolf")
                    val password = Mutable.someString("asdfghjkl")
                    val password1 = Mutable.someString("asdfghjkl")
                    var showPassword by remember { mutableStateOf(false) }
                    val passwordSame by remember {
                        derivedStateOf {
                            password.value == password1.value
                        }
                    }
                    Row(
                        modifier = Modifier.align(Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icons.Default.ArrowBackIosNew.CxIconButton {
                            navController.popBackStack()
                        }
                        "Register new Account".CxText(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    username.CxOutlinedBasicTextField(
                        label = "Username"
                    )
                    password.CxOutlinedBasicTextField(
                        label = "Password",
                        visualTransformation = if (showPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            if (showPassword) Icons.Default.Visibility.CxIconButton {
                                showPassword = false
                            }
                            else Icons.Default.VisibilityOff.CxIconButton {
                                showPassword = true
                            }
                        }
                    )
                    password1.CxOutlinedBasicTextField(
                        label = "Password again",
                        visualTransformation = if (showPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        isError = passwordSame.not(),
                        trailingIcon = {
                            if (showPassword) Icons.Default.Visibility.CxIconButton {
                                showPassword = false
                            }
                            else Icons.Default.VisibilityOff.CxIconButton {
                                showPassword = true
                            }
                        }
                    )
                    "Register".CxTextButton(
                        enabled = passwordSame
                    ) {
//                        httpClient
                        // send register request
//                        navController.login()
                    }
                }
            }
        }
}