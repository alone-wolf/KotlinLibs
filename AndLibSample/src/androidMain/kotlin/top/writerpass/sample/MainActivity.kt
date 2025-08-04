package top.writerpass.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import top.writerpass.cmplibrary.compose.FullSizeBox
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.sample.ui.theme.KotlinLibsTheme


object Pages {
    const val Home = "Home"
    const val Mine = "Mine"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinLibsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FullSizeBox(modifier = Modifier.padding(innerPadding)) {
                        "AAA".Text()
                    }
                }
            }
        }
    }
}
