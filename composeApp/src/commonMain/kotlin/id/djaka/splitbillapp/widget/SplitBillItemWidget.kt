package id.djaka.splitbillapp.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import id.djaka.splitbillapp.platform.Spacing
import id.djaka.splitbillapp.service.bill.BillModel
import id.djaka.splitbillapp.service.trip.TripModel
import id.djaka.splitbillapp.util.readableDateYearFormat
import kotlinx.datetime.Instant
import kotlinx.datetime.format

@Composable
fun SplitBillItem(
    model: BillModel,
    onClick: () -> Unit = {},
    tripModel: TripModel? = null
) {
    SplitBillItem(
        name = model.name,
        date = Instant.fromEpochMilliseconds(model.date).format(readableDateYearFormat),
        status = if (model.members.all { it.isPaid }) "All Paid" else "Unpaid",
        people = model.members.size,
        onClick = onClick,
        trip = tripModel?.name
    )
}

@Composable
fun SplitBillItem(
    name: String = "Phoenix Omurice",
    date: String = "1 January 2022",
    status: String = "All Paid",
    people: Int = 3,
    onClick: () -> Unit = {},
    trip: String? = null
) {
    Card(Modifier.fillMaxWidth().clickable { onClick() }) {
        Column(
            Modifier.padding(Spacing.m),
            verticalArrangement = Arrangement.spacedBy(Spacing.xxs)
        ) {
            Text(name, style = MaterialTheme.typography.titleMedium)

            Text(
                "${trip ?: "No Trip"} • $date",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )

            Spacer(Modifier.height(Spacing.xxs))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Badge {
                    Text(status)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
                    modifier = Modifier.clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = Spacing.xs, vertical = Spacing.xxs)
                ) {
                    Text("$people •", style = MaterialTheme.typography.bodyMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
                        repeat(kotlin.math.min(people, 10)) {
                            Box(
                                Modifier.size(16.dp)
                                    .clip(CircleShape)
                                    .background(SolidColor(Color.Gray))
                                    .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                            )
                        }
                    }
                }


            }
        }
    }
}