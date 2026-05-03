package org.wickedtools.safenose.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import org.wickedtools.safenose.R
import org.wickedtools.safenose.feature_fragrance.domain.model.FragranceSummary
import org.wickedtools.safenose.feature_reviews.presentation.viewmodels.AddReviewUiState
import org.wickedtools.safenose.feature_reviews.presentation.viewmodels.AddReviewViewModel

private val SEASONS = listOf("Summer", "Monsoon", "Winter")
private val OCCASIONS = listOf("Daily", "Office", "Date Night", "Gym", "Party", "Formal", "Outdoor", "Casual")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewScreen(
    viewModel: AddReviewViewModel = koinViewModel(),
    onBack: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var step by remember { mutableIntStateOf(1) }

    val primary = colorResource(R.color.primary)

    LaunchedEffect(uiState.isSubmitSuccess) {
        if (uiState.isSubmitSuccess) {
            viewModel.resetForm()
            onSubmit()
        }
    }

    if (uiState.isSubmitting) {
        Box(modifier = Modifier.fillMaxSize().background(colorResource(R.color.background)), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = primary)
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Review", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { if (step > 1) step-- else onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = if (step > 1) "Previous step" else "Go back",
                            tint = Color.White
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
            ReviewFooter(
                step = step,
                canContinue = when (step) {
                    1 -> uiState.selectedFragrance != null
                    2 -> uiState.overallRating > 0
                    else -> true
                },
                onBack = { step-- },
                onContinue = { if (step < 3) step++ else viewModel.onSubmit() },
                primary = primary
            )
        },
        containerColor = colorResource(R.color.background)
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            StepIndicator(currentStep = step, primary = primary)

            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = Color.Red,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            when (step) {
                1 -> StepFragrance(uiState = uiState, onQueryChange = viewModel::onQueryChange, onSelect = viewModel::onFragranceSelected, primary = primary)
                2 -> StepRatings(uiState = uiState, onOverallRating = viewModel::onOverallRatingChanged, onLongevityRank = viewModel::onLongevityRankChanged, onProjectionRank = viewModel::onProjectionRankChanged, onValueRank = viewModel::onValueRankChanged, primary = primary)
                3 -> StepContext(uiState = uiState, onSeasonSelect = viewModel::onSeasonSelected, onOccasionToggle = viewModel::onOccasionToggled, onReviewTextChange = viewModel::onReviewTextChanged, primary = primary)
            }
        }
    }
}

// ── Step indicator ───────────────────────────────────────

@Composable
private fun StepIndicator(currentStep: Int, primary: Color) {
    val steps = listOf("Fragrance", "Ratings", "Context")
    Surface(color = Color.White, shadowElevation = 1.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, label ->
                val stepNum = index + 1
                val isCompleted = currentStep > stepNum
                val isActive = currentStep == stepNum

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.semantics {
                        contentDescription = "Step $stepNum $label${if (isCompleted) " completed" else if (isActive) " current" else ""}"
                    }
                ) {
                    Box(
                        modifier = Modifier.size(28.dp).clip(CircleShape)
                            .background(if (isActive || isCompleted) primary else Color(0xFFEBEBEB)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        } else {
                            Text("$stepNum", color = if (isActive) Color.White else Color(0xFF7A7A7A), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(label, fontSize = 10.sp, fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal, color = if (isActive || isCompleted) primary else Color(0xFF7A7A7A))
                }

                if (index < steps.size - 1) {
                    Box(
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp).padding(bottom = 18.dp).height(2.dp)
                            .background(if (currentStep > stepNum) primary else Color(0xFFEBEBEB), RoundedCornerShape(1.dp))
                    )
                }
            }
        }
    }
}

// ── Step 1: Pick fragrance ───────────────────────────────

@Composable
private fun StepFragrance(
    uiState: AddReviewUiState,
    onQueryChange: (String) -> Unit,
    onSelect: (FragranceSummary) -> Unit,
    primary: Color
) {
    val displayList = if (uiState.searchQuery.length >= 2) uiState.searchResults else uiState.trendingFragrances

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Text("Which fragrance?", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1A1A1A))
        Spacer(Modifier.height(4.dp))
        Text("Search by name or brand", fontSize = 13.sp, color = Color(0xFF7A7A7A))
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onQueryChange,
            label = { Text("Fragrance name") },
            placeholder = { Text("e.g. Sauvage, Dior…") },
            leadingIcon = {
                if (uiState.isSearching)
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = primary)
                else
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF7A7A7A))
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primary, unfocusedBorderColor = Color(0xFFEBEBEB), focusedLabelColor = primary)
        )

        val selectedFragrance = uiState.selectedFragrance
        if (selectedFragrance != null) {
            Spacer(Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = primary.copy(alpha = 0.08f),
                border = BorderStroke(1.dp, primary.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(primary), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                    Column {
                        Text("Selected", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colorResource(R.color.primaryAccent))
                        Text("${selectedFragrance.name} – ${selectedFragrance.brandName}", fontSize = 14.sp, color = Color(0xFF1A1A1A))
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = if (uiState.searchQuery.length >= 2) "${displayList.size} results" else "Trending fragrances",
            fontSize = 12.sp, color = Color(0xFF7A7A7A), modifier = Modifier.padding(bottom = 8.dp)
        )

        if (displayList.isEmpty() && uiState.searchQuery.length >= 2 && !uiState.isSearching) {
            Text("No fragrances found", fontSize = 13.sp, color = Color(0xFF7A7A7A), modifier = Modifier.padding(vertical = 16.dp))
        } else if (displayList.isNotEmpty()) {
            Surface(shape = RoundedCornerShape(12.dp), shadowElevation = 2.dp, color = Color.White, modifier = Modifier.fillMaxWidth()) {
                Column {
                    displayList.forEachIndexed { index, fragrance ->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .clickable { onSelect(fragrance) }
                                .background(if (uiState.selectedFragrance?.id == fragrance.id) primary.copy(alpha = 0.06f) else Color.White)
                                .padding(horizontal = 16.dp, vertical = 14.dp)
                                .semantics { contentDescription = "${fragrance.name} by ${fragrance.brandName}" },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFD4E8E1)), contentAlignment = Alignment.Center) {
                                Text(fragrance.name.first().toString(), fontWeight = FontWeight.Bold, color = primary, fontSize = 14.sp)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(fragrance.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                                Text("${fragrance.brandName}${fragrance.concentration?.let { " · $it" } ?: ""}", fontSize = 12.sp, color = Color(0xFF7A7A7A))
                            }
                            if (uiState.selectedFragrance?.id == fragrance.id) {
                                Icon(Icons.Default.Check, contentDescription = "Selected", tint = primary, modifier = Modifier.size(18.dp))
                            }
                        }
                        if (index < displayList.size - 1) HorizontalDivider(color = Color(0xFFEBEBEB), thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}

// ── Step 2: Ratings ──────────────────────────────────────

@Composable
private fun StepRatings(
    uiState: AddReviewUiState,
    onOverallRating: (Int) -> Unit,
    onLongevityRank: (String) -> Unit,
    onProjectionRank: (String) -> Unit,
    onValueRank: (String) -> Unit,
    primary: Color
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Rate ${uiState.selectedFragrance?.name ?: ""}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1A1A1A))
        Row {
            Text("Your experience in ", fontSize = 13.sp, color = Color(0xFF7A7A7A))
            Text(uiState.city.ifBlank { "your city" }, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = primary)
        }

        Surface(shape = RoundedCornerShape(16.dp), shadowElevation = 2.dp, color = Color.White, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Overall Rating", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7A7A7A), letterSpacing = 0.5.sp)
                Spacer(Modifier.height(12.dp))
                StarPicker(rating = uiState.overallRating, onRate = onOverallRating)
                Spacer(Modifier.height(6.dp))
                Text(
                    text = when (uiState.overallRating) { 1 -> "Poor"; 2 -> "Below average"; 3 -> "Average"; 4 -> "Good"; 5 -> "Excellent"; else -> "Tap to rate" },
                    fontSize = 12.sp,
                    color = if (uiState.overallRating > 0) primary else Color(0xFF7A7A7A)
                )
            }
        }

        Surface(shape = RoundedCornerShape(16.dp), shadowElevation = 2.dp, color = Color.White, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Performance in ${uiState.city.ifBlank { "your city" }}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7A7A7A), letterSpacing = 0.5.sp)
                Spacer(Modifier.height(16.dp))
                GradePicker(label = "Longevity", selected = uiState.longevityRank, onSelect = onLongevityRank, activeColor = primary)
                GradePicker(label = "Sillage (Projection)", selected = uiState.projectionRank, onSelect = onProjectionRank, activeColor = Color(0xFF8E44AD))
                GradePicker(label = "Value for Money", selected = uiState.valueRank, onSelect = onValueRank, activeColor = colorResource(R.color.secondaryAccent))
            }
        }

        Surface(shape = RoundedCornerShape(12.dp), color = primary.copy(alpha = 0.07f), modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.Info, contentDescription = null, tint = primary, modifier = Modifier.size(18.dp).padding(top = 1.dp))
                Text(
                    text = "Humidity & temperature in ${uiState.city.ifBlank { "your city" }} affect these numbers. Your honest rating helps others choose better.",
                    fontSize = 12.sp, color = Color(0xFF7A7A7A), lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun StarPicker(rating: Int, onRate: (Int) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.semantics { contentDescription = "Overall rating: $rating out of 5 stars" }
    ) {
        (1..5).forEach { star ->
            IconButton(onClick = { onRate(star) }, modifier = Modifier.size(48.dp)) {
                Icon(
                    imageVector = if (star <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "$star star${if (star == 1) "" else "s"}",
                    tint = if (star <= rating) Color(0xFFFFD700) else Color(0xFFCCCCCC),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

private val GRADE_LABELS = mapOf("S" to "Excellent", "A" to "Good", "B" to "Average", "C" to "Poor")

@Composable
private fun GradePicker(label: String, selected: String, onSelect: (String) -> Unit, activeColor: Color) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1A1A))
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("S", "A", "B", "C").forEach { grade ->
                val isSelected = selected == grade
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) activeColor else Color.White,
                    border = BorderStroke(1.5.dp, if (isSelected) activeColor else Color(0xFFEBEBEB)),
                    modifier = Modifier.weight(1f).clickable { onSelect(grade) }.semantics { contentDescription = "$label $grade ${GRADE_LABELS[grade]}${if (isSelected) ", selected" else ""}" }
                ) {
                    Column(modifier = Modifier.padding(vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(grade, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = if (isSelected) Color.White else Color(0xFF1A1A1A))
                        Text(GRADE_LABELS[grade] ?: "", fontSize = 9.sp, color = if (isSelected) Color.White.copy(alpha = 0.85f) else Color(0xFF7A7A7A))
                    }
                }
            }
        }
    }
}

// ── Step 3: Context ──────────────────────────────────────

@Composable
private fun StepContext(
    uiState: AddReviewUiState,
    onSeasonSelect: (String) -> Unit,
    onOccasionToggle: (String) -> Unit,
    onReviewTextChange: (String) -> Unit,
    primary: Color
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Add context", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1A1A1A))

        Surface(shape = RoundedCornerShape(16.dp), shadowElevation = 2.dp, color = Color.White, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Season worn", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SEASONS.forEach { season ->
                        val isSelected = uiState.selectedSeason == season
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) primary else Color.White,
                            border = BorderStroke(1.5.dp, if (isSelected) primary else Color(0xFFEBEBEB)),
                            modifier = Modifier.clickable { onSeasonSelect(season) }.semantics { contentDescription = "$season${if (isSelected) ", selected" else ""}" }
                        ) {
                            Text(season, modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp), fontSize = 13.sp, color = if (isSelected) Color.White else Color(0xFF1A1A1A), fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }
        }

        Surface(shape = RoundedCornerShape(16.dp), shadowElevation = 2.dp, color = Color.White, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Occasion", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                    Spacer(Modifier.width(6.dp))
                    Text("(select all that apply)", fontSize = 11.sp, color = Color(0xFF7A7A7A))
                }
                Spacer(Modifier.height(10.dp))
                OCCASIONS.chunked(4).forEach { rowItems ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 8.dp)) {
                        rowItems.forEach { occasion ->
                            val isSelected = occasion in uiState.selectedOccasions
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) primary.copy(alpha = 0.10f) else Color.White,
                                border = BorderStroke(1.5.dp, if (isSelected) primary else Color(0xFFEBEBEB)),
                                modifier = Modifier.clickable { onOccasionToggle(occasion) }.semantics { contentDescription = "$occasion${if (isSelected) ", selected" else ""}" }
                            ) {
                                Text(occasion, modifier = Modifier.padding(horizontal = 11.dp, vertical = 8.dp), fontSize = 12.sp, color = if (isSelected) primary else Color(0xFF1A1A1A), fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }
                }
            }
        }

        Surface(shape = RoundedCornerShape(16.dp), shadowElevation = 2.dp, color = Color.White, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Your Review", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = uiState.reviewText,
                    onValueChange = { if (it.length <= 500) onReviewTextChange(it) },
                    placeholder = { Text("How did it perform in ${uiState.city.ifBlank { "your city" }}'s climate?", fontSize = 13.sp) },
                    minLines = 4,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primary, unfocusedBorderColor = Color(0xFFEBEBEB))
                )
                Text("${uiState.reviewText.length}/500", fontSize = 11.sp, color = Color(0xFF7A7A7A), modifier = Modifier.align(Alignment.End).padding(top = 4.dp))
            }
        }
    }
}

// ── Footer ───────────────────────────────────────────────

@Composable
private fun ReviewFooter(step: Int, canContinue: Boolean, onBack: () -> Unit, onContinue: () -> Unit, primary: Color) {
    Surface(color = Color.White, shadowElevation = 8.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp).navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (step > 1) {
                OutlinedButton(
                    onClick = onBack, modifier = Modifier.weight(1f).height(52.dp), shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, Color(0xFFCCCCCC))
                ) {
                    Text("Back", color = Color(0xFF1A1A1A), fontWeight = FontWeight.SemiBold)
                }
            }
            Button(
                onClick = onContinue, enabled = canContinue,
                modifier = Modifier.weight(if (step > 1) 2f else 1f).height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primary, disabledContainerColor = Color(0xFFB0B0B0)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 2.dp)
            ) {
                Text(if (step < 3) "Continue →" else "Post Review", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ── Success state ────────────────────────────────────────

@Composable
private fun SuccessState(city: String, onDone: () -> Unit) {
    val primary = colorResource(R.color.primary)
    val primaryDark = colorResource(R.color.primaryAccent)

    Column(
        modifier = Modifier.fillMaxSize().background(colorResource(R.color.background)).padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(primary), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Check, contentDescription = "Review submitted successfully", tint = Color.White, modifier = Modifier.size(40.dp))
        }
        Spacer(Modifier.height(24.dp))
        Text("Review Posted!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = primaryDark)
        Spacer(Modifier.height(10.dp))
        Text("Your experience from $city helps others choose better.", fontSize = 14.sp, color = Color(0xFF7A7A7A), textAlign = TextAlign.Center, lineHeight = 22.sp)
        Spacer(Modifier.height(32.dp))
        Button(onClick = onDone, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = primary), modifier = Modifier.fillMaxWidth(0.65f).height(52.dp)) {
            Text("Done", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}
