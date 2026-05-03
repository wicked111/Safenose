package org.wickedtools.safenose.screens

import android.annotation.SuppressLint
import org.wickedtools.safenose.components.HomeScreen_LatestReviewsCard
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import org.wickedtools.safenose.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.wickedtools.safenose.components.HomeScreen_PopularFragranceCard


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true, name = "I love it")
@Composable
fun HomeScreen(){
    var selectedItem by remember { mutableStateOf(0) }

    when (selectedItem) {
        1 -> AddReviewScreen(onBack = { selectedItem = 0 }, onSubmit = { selectedItem = 0 })
        2 -> ProfileScreen(selectedItem = selectedItem, onItemSelected = { selectedItem = it })
        else -> Scaffold(
            topBar = { SafenoseTopAppBar() },
            bottomBar = { SafenoseBottomNav(selectedItem = selectedItem, onItemSelected = { selectedItem = it }) }
        ) {
            Column(modifier = Modifier.padding(top = 64.dp)) {
                SimpleSearchBar(
                    textFieldState = remember { TextFieldState() },
                    onSearch = {},
                    searchResults = listOf()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Popular Fragrances",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp),
                    color = colorResource(id = R.color.primaryAccent)
                )

                Spacer(modifier = Modifier.height(8.dp))

                PopularFragrance()

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Latest Reviews",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                    color = colorResource(id = R.color.primaryAccent)
                )

                LazyColumn {
                    items(10) {
                        HomeScreen_LatestReviewsCard("https://picsum.photos/id/22/200/300")
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafenoseTopAppBar() {
    TopAppBar(
        title = {
            // You can use Text or an Image for your logo
            Text("Safenose", fontWeight = FontWeight.Bold)
        },
        actions = {
            IconButton(onClick = { /* Handle search click */ }) {
                Icon(Icons.Default.Favorite, contentDescription = "Favourites")
            }
        }         ,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.primary)
        )



    )
}

@Composable
fun SafenoseBottomNav(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    val items = listOf("Home", "Add Review", "Profile")
    val icons = listOf(Icons.Default.Home, Icons.Default.Edit, Icons.Default.Person)

    NavigationBar(containerColor = colorResource(R.color.background)) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorResource(R.color.primary),
                    unselectedIconColor = Color.Black,
                    selectedTextColor = colorResource(R.color.primary),
                    unselectedTextColor = Color.Black,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
) {
    // Controls expansion state of the search bar
    var expanded by rememberSaveable { mutableStateOf(false) }


    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { traversalIndex = 0f }
            .padding(8.dp),


        shape = RoundedCornerShape(12.dp),

        inputField = {
            SearchBarDefaults.InputField(
                query = textFieldState.text.toString(),
                onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                onSearch = {
                    onSearch(textFieldState.text.toString())
                    expanded = false
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("Search for a fragrance or brand") },
                leadingIcon = { Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )}
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        // Display search results in a scrollable column
        Column(Modifier.verticalScroll(rememberScrollState()).heightIn(50.dp)) {
            searchResults.forEach { result ->
                ListItem(
                    headlineContent = { Text(result) },
                    modifier = Modifier
                        .clickable {
                            textFieldState.edit { replace(0, length, result) }
                            expanded = false
                        }
                        .fillMaxWidth()
                )
            }
        }
    }

}



@Composable
fun PopularFragrance(){

    val fragranceName : List<String> = listOf("Fragrance 1", "Fragrance 2", "Fragrance 3", "Fragrance 4", "Fragrance 5", "Fragrance 6")
    val fragranceIcon : List<ImageVector> = listOf(Icons.Default.Favorite, Icons.Default.Star, Icons.Default.Favorite, Icons.Default.Star, Icons.Default.Favorite, Icons.Default.Star,Icons.Default.AccountBox)

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
         for (index in fragranceName.indices){
             item {
                 HomeScreen_PopularFragranceCard(fragranceName, fragranceIcon, index)
             }
         }
    }

}





