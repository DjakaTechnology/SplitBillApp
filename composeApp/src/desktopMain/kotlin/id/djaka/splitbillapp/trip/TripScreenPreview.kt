package id.djaka.splitbillapp.trip

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import id.djaka.splitbillapp.platform.CoreTheme

@Composable
@Preview
fun TripScreenPreview() {
    CoreTheme {
        TripScreenWidget(
            tripData = listOf(),
            memberSummary = listOf()
        )
    }
}