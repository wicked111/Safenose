package org.wickedtools.safenose.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import org.wickedtools.safenose.R


@Composable
fun RatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    color: Color = colorResource(R.color.star_color)
) {
    Row(modifier = modifier) {
        // We loop 5 times to represent the 5 stars
        for (i in 1..5) {
            val starIcon = when {
                // Check if the current star should be filled
                i <= rating -> Icons.Default.Star
                // Check if the current star should be half-filled
                i > rating && i - 1 < rating -> Icons.Default.StarHalf
                // Otherwise, it's an empty star
                else -> Icons.Default.StarOutline
            }
            Icon(
                imageVector = starIcon,
                contentDescription = "$i star",
                tint = color
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RatingBarPreview(){

    RatingBar(5f)
}