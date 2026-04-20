package org.wickedtools.safenose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import org.wickedtools.safenose.R

@Composable
fun HomeScreen_PopularFragranceCard(fragranceName : List<String>, fragranceImage : List<ImageVector>, index:Int){

    Box(modifier = Modifier
        .width(180.dp)
        .fillMaxHeight()
        .background(color = colorResource(id = R.color.background), RoundedCornerShape(8.dp))){

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(imageVector = fragranceImage[index], contentDescription = "Fragrance Icon")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = fragranceName[index])

        }



    }

}