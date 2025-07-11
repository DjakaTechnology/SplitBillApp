package id.djaka.splitbillapp.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.djaka.splitbillapp.util.createRandomColorFromName

@Composable
fun PeopleWidget(
    text: String = "",
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    onClickClose: () -> Unit = {},
    isShowClose: Boolean = false,
    isShowLabel: Boolean = true,
    size: Dp = 64.dp,
    activeBorderColor: Color = MaterialTheme.colorScheme.primary,
    backgroundAlpha: Float? = null
) {
    val color = createRandomColorFromName(text).let {
        if (backgroundAlpha != null) {
            it.copy(alpha = backgroundAlpha)
        } else it
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.widthIn(max = size)
    ) {
        Box {
            Box(
                Modifier.size(size)
                    .clip(CircleShape)
                    .background(color = color)
                    .border(
                        if (isSelected) 8.dp else 0.dp,
                        if (isSelected) activeBorderColor else MaterialTheme.colorScheme.surface,
                        CircleShape
                    )
                    .clickable {
                        onClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text.firstOrNull()?.toString().orEmpty(),
                    textAlign = TextAlign.Center,
                    fontSize = (size.value / 2).sp,
                )
            }

            if (isShowClose) {
                Icon(
                    Icons.Filled.Close,
                    "delete",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant
                        ).align(Alignment.TopEnd).clickable {
                            onClickClose()
                        }
                )
            }
        }
        if (isShowLabel) {
            Text(text, overflow = TextOverflow.Ellipsis, maxLines = 1)
        }
    }
}