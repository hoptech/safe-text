package br.com.hoptech.safetext

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.hoptech.safetext.ui.SafeTextScreen
import br.com.hoptech.safetext.ui.theme.SafeTextTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SafeTextTheme {
                SafeTextScreen()
            }
        }
    }
}
