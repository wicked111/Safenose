package org.wickedtools.safenose.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import org.wickedtools.safenose.R
import org.wickedtools.safenose.feature_authentication.presentation.viewmodels.AuthViewModel
import org.wickedtools.safenose.feature_profile.presentation.viewmodels.ProfileViewModel
import org.wickedtools.safenose.feature_reviews.domain.model.UserReview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    val profileViewModel: ProfileViewModel = koinViewModel()
    val authViewModel: AuthViewModel = koinViewModel()
    val uiState by profileViewModel.uiState.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    var editFullName by remember { mutableStateOf("") }
    var editUsername by remember { mutableStateOf("") }
    var editCity by remember { mutableStateOf("") }

    val primary = colorResource(R.color.primary)
    val primaryAccent = colorResource(R.color.primaryAccent)

    LaunchedEffect(uiState.isUpdateSuccess) {
        if (uiState.isUpdateSuccess) {
            isEditing = false
            profileViewModel.clearSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My Profile", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(
                        onClick = {
                            if (isEditing) {
                                profileViewModel.onUpdateClicked(
                                    username = editUsername.ifBlank { null },
                                    fullName = editFullName.ifBlank { null },
                                    city = editCity,
                                    avatarUrl = null
                                )
                            } else {
                                editFullName = uiState.user?.fullName ?: ""
                                editUsername = uiState.user?.username ?: ""
                                editCity = uiState.user?.city ?: ""
                                isEditing = true
                            }
                        }
                    ) {
                        Text(
                            text = if (isEditing) "Save" else "Edit",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            SafenoseBottomNav(
                selectedItem = selectedItem,
                onItemSelected = onItemSelected
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 56.dp), // room for logout button
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(24.dp))

                val initial = (uiState.user?.username?.firstOrNull()
                    ?: uiState.user?.fullName?.firstOrNull()
                    ?: '?').toString().uppercase()

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8913A)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = initial, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 32.sp)
                }

                Spacer(Modifier.height(12.dp))

                if (!isEditing) {
                    Text(
                        text = uiState.user?.fullName ?: uiState.user?.username ?: "—",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(text = "@${uiState.user?.username ?: ""}", color = primaryAccent, fontSize = 14.sp)
                    Spacer(Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = primary.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, primary.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = primary, modifier = Modifier.size(14.dp))
                            Text(text = uiState.user?.city ?: "", color = primary, fontSize = 13.sp)
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "${uiState.reviews.size}", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                            Text(text = "Reviews", fontSize = 13.sp, color = Color.Gray)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    TabRow(selectedTabIndex = selectedTab, containerColor = Color.White, contentColor = primary) {
                        Tab(
                            selected = selectedTab == 0, onClick = { selectedTab = 0 },
                            text = { Text("My Reviews", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) }
                        )
                        Tab(
                            selected = selectedTab == 1, onClick = { selectedTab = 1 },
                            text = { Text("Liked", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) }
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    when (selectedTab) {
                        0 -> {
                            if (uiState.reviews.isEmpty()) {
                                Text(text = "No reviews yet", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(top = 20.dp))
                            } else {
                                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    uiState.reviews.forEach { review ->
                                        ProfileReviewCard(review = review, primary = primary)
                                    }
                                }
                            }
                        }
                        1 -> Text(text = "Liked fragrances — coming soon", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(top = 20.dp))
                    }

                    Spacer(Modifier.height(8.dp))

                } else {
                    Spacer(Modifier.height(8.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Full Name", fontSize = 13.sp)
                        OutlinedTextField(value = editFullName, onValueChange = { editFullName = it }, placeholder = { Text("Your full name") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(4.dp))
                        Text("Username", fontSize = 13.sp)
                        OutlinedTextField(value = editUsername, onValueChange = { editUsername = it }, placeholder = { Text("e.g. rohan_mumbai") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(4.dp))
                        Text("City", fontSize = 13.sp)
                        OutlinedTextField(
                            value = editCity, onValueChange = { editCity = it },
                            placeholder = { Text("Your city") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                            singleLine = true, modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        if (uiState.error != null) {
                            Text(text = uiState.error!!, color = Color.Red, fontSize = 13.sp)
                            Spacer(Modifier.height(4.dp))
                        }
                        if (uiState.isLoading) {
                            CircularProgressIndicator(color = primary, modifier = Modifier.size(24.dp).align(Alignment.CenterHorizontally), strokeWidth = 2.dp)
                            Spacer(Modifier.height(8.dp))
                        }
                        OutlinedButton(
                            onClick = { isEditing = false; profileViewModel.clearError() },
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, Color.Gray)
                        ) {
                            Text("Cancel", color = Color.Gray)
                        }
                    }
                }
            }

            // Logout pinned to bottom
            TextButton(
                onClick = { authViewModel.onLogoutClicked() },
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().navigationBarsPadding().padding(bottom = 4.dp)
            ) {
                Text("Log Out", color = Color.Red, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun ProfileReviewCard(review: UserReview, primary: Color) {
    Surface(shape = RoundedCornerShape(12.dp), shadowElevation = 2.dp, color = Color.White, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(review.fragranceName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1A1A1A))
                    Text(review.brandName, fontSize = 12.sp, color = Color(0xFF7A7A7A))
                }
                Text(review.createdAt, fontSize = 11.sp, color = Color(0xFF7A7A7A))
            }

            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                (1..5).forEach { star ->
                    Icon(
                        imageVector = if (star <= review.rating) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = null,
                        tint = if (star <= review.rating) Color(0xFFFFD700) else Color(0xFFCCCCCC),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            val grades = listOfNotNull(
                review.longevityRank?.let { "L:$it" },
                review.projectionRank?.let { "P:$it" },
                review.valueRank?.let { "V:$it" }
            )
            if (grades.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    grades.forEach { badge ->
                        Surface(shape = RoundedCornerShape(6.dp), color = primary.copy(alpha = 0.1f)) {
                            Text(badge, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                        }
                    }
                    review.seasonWorn?.let {
                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFF5F5F5)) {
                            Text(it, fontSize = 11.sp, color = Color(0xFF7A7A7A), modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                        }
                    }
                }
            }

            val reviewText = review.reviewText
            if (!reviewText.isNullOrBlank()) {
                Spacer(Modifier.height(8.dp))
                HorizontalDivider(color = Color(0xFFEBEBEB))
                Spacer(Modifier.height(8.dp))
                Text(reviewText, fontSize = 13.sp, color = Color(0xFF4A4A4A), lineHeight = 20.sp)
            }
        }
    }
}

