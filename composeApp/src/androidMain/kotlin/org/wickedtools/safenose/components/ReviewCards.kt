package org.wickedtools.safenose.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.wickedtools.safenose.R




@Composable
fun HomeScreen_LatestReviewsCard(imageUrl: String) {
    Card(
        colors = CardDefaults .cardColors(
            containerColor = colorResource(R.color.background) // sets the card background
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {

        Column (modifier = Modifier.fillMaxSize()){

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Profile image
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Profile Picture in Home screen",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(6.dp))

                // Name + Location Column
                Column(modifier = Modifier.padding(2.dp)) {
                    Text(
                        text = "UserName",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location"
                        )
                        Text(
                            text = "Location",
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                // Push RatingBar to the right
                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .padding(end = 4.dp) // <-- 4dp from the right edge
                ) {
                    RatingBar(5f)
                }
            }

            Text(color = colorResource(R.color.primaryAccent),
                text = "Rasasi Hawas",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )

            Text(text = stringResource(R.string.dummytext),
                modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 8.dp)
            )












        }





    }


}


@Preview(showBackground = true)
@Composable
fun ReviewCardsPreview(){
    HomeScreen_LatestReviewsCard(
        "https://fastly.picsum.photos/id/22/4434/3729.jpg?hmac=fjZdkSMZJNFgsoDh8Qo5zdA_nSGUAWvKLyyqmEt2xs0")
}