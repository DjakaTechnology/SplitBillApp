package id.djaka.splitbillapp.home

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import id.djaka.splitbillapp.platform.CoreTheme
import id.djaka.splitbillapp.service.bill.BillModel
import id.djaka.splitbillapp.service.trip.TripModel
import kotlinx.datetime.Clock

@Preview
@Composable
fun HomeScreenPreview() {
    CoreTheme {
        HomeWidget(
            data = listOf(
                BillModel(
                    name = "Name",
                    members = listOf(
                        BillModel.Member(
                            id = "id",
                            name = "Name",
                            isPaid = false
                        )
                    ),
                    items = listOf(
                        BillModel.Item(
                            id = "id",
                            name = "Name",
                            price = 10000.0,
                            qty = 1,
                            memberIds = setOf("id")
                        )
                    )
                )
            ),
            tripData = listOf(
                TripModel(
                    id = "id",
                    name = "Name",
                    startDate = Clock.System.now().toEpochMilliseconds(),
                    endDate = Clock.System.now().toEpochMilliseconds()
                )
            ),
            tripMap = emptyMap()
        )
    }
}