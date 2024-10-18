package id.djaka.splitbillapp

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import id.djaka.splitbillapp.home.HomeScreen
import id.djaka.splitbillapp.platform.CoreTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

const val WebClientId = "435873001124-dbqi5bgapmugqa2nke7dl1uqmcavfht4.apps.googleusercontent.com"

@Composable
@Preview
fun App() {
    CoreTheme {
        Navigator(HomeScreen())
    }
}