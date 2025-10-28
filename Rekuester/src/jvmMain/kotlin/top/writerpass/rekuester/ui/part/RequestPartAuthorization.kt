package top.writerpass.rekuester.ui.part

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import top.writerpass.cmplibrary.compose.DropDownMenu
import top.writerpass.cmplibrary.compose.FullWidthColumn
import top.writerpass.cmplibrary.compose.FullWidthRow
import top.writerpass.cmplibrary.compose.OutlinedBasicTextField
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.utils.Mutable
import top.writerpass.rekuester.LocalApiViewModel
import top.writerpass.rekuester.ui.page.AuthTypes

@Composable
fun RequestPartAuthorization(){
    val apiViewModel = LocalApiViewModel.current
    val apiState by apiViewModel.apiStateFlow.collectAsState()
    FullWidthRow {
        apiState.authType.DropDownMenu(
            entities = AuthTypes.typeMap,
            any2String = { label }
        )
        FullWidthColumn {
            when(apiState.authType.value){
                AuthTypes.InheritAuthFromParent -> {
                    "Inherit Auth From Parent".Text()
                }
                AuthTypes.NoAuth -> {
                    "No Auth".Text()
                }
                AuthTypes.Basic -> {
                    val userName = Mutable.someString("")
                    val password = Mutable.someString("")
                    Row {
                        userName.OutlinedBasicTextField(label = "Username")
                    }
                    Row {
                        password.OutlinedBasicTextField(label = "Password")
                    }
                }
                AuthTypes.Bearer -> {
                    val token = Mutable.someString("")
                    Row {
                        token.OutlinedBasicTextField(label = "Token")
                    }
                }
                AuthTypes.JWT -> {
                    val secret = Mutable.someString("")
                    Row {
                        secret.OutlinedBasicTextField(label = "Token")
                    }
                }
                AuthTypes.ApiKey -> {
                    val key = Mutable.someString("")
                    val value = Mutable.someString("")
                    val addTo = Mutable.someString("")
                    Row {
                        key.OutlinedBasicTextField(label = "Username")
                    }
                    Row {
                        value.OutlinedBasicTextField(label = "Password")
                    }
                    Row {
                        addTo.OutlinedBasicTextField(label = "Add to")
                    }
                }
                else->{}
            }
        }
    }
}