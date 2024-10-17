package id.djaka.splitbillapp

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import id.djaka.splitbillapp.home.HomeScreen
import id.djaka.splitbillapp.platform.CoreTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    CoreTheme {
        Navigator(HomeScreen())
    }
}