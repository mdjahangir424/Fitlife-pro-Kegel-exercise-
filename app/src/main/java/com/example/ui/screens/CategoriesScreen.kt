package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material.icons.filled.SportsHandball
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WorkoutEntity
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.util.LanguageStrings

data class CategoryItem(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val subBn: String,
    val subEn: String,
    val color: Color,
    val icon: ImageVector
)

@Composable
fun CategoriesScreen(
    workouts: List<WorkoutEntity>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    isBangla: Boolean,
    onCategoryClick: (WorkoutEntity) -> Unit
) {
    val categories = listOf(
        CategoryItem("full_body", "Full Body", "Full Body", "সম্পূর্ণ শরীর", "Full Body", Color(0xFFFF6D00), Icons.Default.DirectionsRun),
        CategoryItem("abs", "Abs & Core", "Abs & Core", "পেট ও কোর", "Abs & Core", Color(0xFF00B0FF), Icons.Default.FitnessCenter),
        CategoryItem("chest", "Chest", "Chest", "বুক", "Chest", Color(0xFFFF5252), Icons.Default.AccessibilityNew),
        CategoryItem("arms", "Arms", "Arms", "বাহু", "Arms", Color(0xFF00C853), Icons.Default.SportsHandball),
        CategoryItem("shoulders", "Shoulders", "Shoulders", "কাঁধ", "Shoulders", Color(0xFF0091EA), Icons.Default.SportsGymnastics),
        CategoryItem("back", "Back", "Back", "পিঠ", "Back", Color(0xFF304FFE), Icons.Default.SportsKabaddi),
        CategoryItem("legs", "Legs", "Legs", "পা", "Legs", Color(0xFF7C4DFF), Icons.Default.DirectionsRun),
        CategoryItem("stretching", "Stretching", "Stretching", "স্ট্রেচিং", "Stretching", Color(0xFF00B4D8), Icons.Default.SelfImprovement)
    )

    val filtered = categories.filter {
        it.titleBn.contains(searchQuery, ignoreCase = true) ||
                it.titleEn.contains(searchQuery, ignoreCase = true) ||
                it.subBn.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isBangla) "ওয়ার্কআউট ক্যাটাগরি" else "Workout Categories",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("categories_search_input"),
            placeholder = {
                Text(
                    text = LanguageStrings.get("search_category", isBangla),
                    color = TextMuted,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextMuted
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = NeonEmerald,
                unfocusedBorderColor = SurfaceBorder,
                focusedContainerColor = SurfaceCard,
                unfocusedContainerColor = SurfaceCard
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filtered) { cat ->
                val matchedWorkout = workouts.firstOrNull { it.category == cat.id } ?: workouts.firstOrNull()

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp))
                        .clickable {
                            if (matchedWorkout != null) {
                                onCategoryClick(matchedWorkout)
                            }
                        }
                        .testTag("category_card_${cat.id}"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(cat.color.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = cat.icon,
                                contentDescription = cat.titleEn,
                                tint = cat.color,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (isBangla) cat.titleBn else cat.titleEn,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = if (isBangla) cat.subBn else cat.subEn,
                            fontSize = 11.sp,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
