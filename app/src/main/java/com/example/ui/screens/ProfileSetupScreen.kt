package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.util.LanguageStrings

@Composable
fun ProfileSetupScreen(
    initialName: String,
    initialAge: String,
    initialExp: String,
    initialGoal: String,
    isBangla: Boolean,
    onSave: (name: String, age: String, exp: String, goal: String) -> Unit
) {
    var name by remember { mutableStateOf(initialName.ifEmpty { if (isBangla) "জাহাঙ্গীর আলম" else "Jahangir Alam" }) }
    var ageRange by remember { mutableStateOf(initialAge.ifEmpty { "25 - 35" }) }
    var expLevel by remember { mutableStateOf(initialExp.ifEmpty { if (isBangla) "শুরু করছি (Beginner)" else "Beginner" }) }
    var goal by remember { mutableStateOf(initialGoal.ifEmpty { if (isBangla) "সামগ্রিক ফিটনেস" else "General Fitness" }) }

    var ageExpanded by remember { mutableStateOf(false) }
    var expExpanded by remember { mutableStateOf(false) }
    var goalExpanded by remember { mutableStateOf(false) }

    val ageOptions = listOf("18 - 24", "25 - 35", "36 - 45", "46+")
    val expOptions = if (isBangla) {
        listOf("শুরু করছি (Beginner)", "মধ্যবর্তী (Intermediate)", "অভিজ্ঞ (Advanced)")
    } else {
        listOf("Beginner", "Intermediate", "Advanced")
    }
    val goalOptions = if (isBangla) {
        listOf("সামগ্রিক ফিটনেস", "ওজন নিয়ন্ত্রণ", "শক্তি ও পেশী বৃদ্ধি", "স্ট্যামিনা বাড়ানো")
    } else {
        listOf("General Fitness", "Weight Loss", "Build Strength & Muscle", "Increase Stamina")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = LanguageStrings.get("profile_title", isBangla),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = LanguageStrings.get("profile_sub", isBangla),
            fontSize = 13.sp,
            color = TextMuted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Avatar placeholder
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color(0xFF1E2630))
                .border(2.dp, NeonEmerald, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                tint = NeonEmerald,
                modifier = Modifier.size(54.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Name field
        Text(
            text = LanguageStrings.get("name_label", isBangla),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextMuted,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("profile_name_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = NeonEmerald,
                unfocusedBorderColor = SurfaceBorder,
                focusedContainerColor = SurfaceCard,
                unfocusedContainerColor = SurfaceCard
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Age bracket dropdown
        Text(
            text = LanguageStrings.get("age_label", isBangla),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextMuted,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                    .clickable { ageExpanded = true }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = ageRange, color = TextPrimary, fontSize = 15.sp)
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown", tint = TextMuted)
            }
            DropdownMenu(
                expanded = ageExpanded,
                onDismissRequest = { ageExpanded = false },
                modifier = Modifier.background(SurfaceCard)
            ) {
                ageOptions.forEach { opt ->
                    DropdownMenuItem(
                        text = { Text(opt, color = TextPrimary) },
                        onClick = {
                            ageRange = opt
                            ageExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Experience dropdown
        Text(
            text = LanguageStrings.get("experience_label", isBangla),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextMuted,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                    .clickable { expExpanded = true }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = expLevel, color = TextPrimary, fontSize = 15.sp)
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown", tint = TextMuted)
            }
            DropdownMenu(
                expanded = expExpanded,
                onDismissRequest = { expExpanded = false },
                modifier = Modifier.background(SurfaceCard)
            ) {
                expOptions.forEach { opt ->
                    DropdownMenuItem(
                        text = { Text(opt, color = TextPrimary) },
                        onClick = {
                            expLevel = opt
                            expExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Goal dropdown
        Text(
            text = LanguageStrings.get("goal_label", isBangla),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextMuted,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                    .clickable { goalExpanded = true }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = goal, color = TextPrimary, fontSize = 15.sp)
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown", tint = TextMuted)
            }
            DropdownMenu(
                expanded = goalExpanded,
                onDismissRequest = { goalExpanded = false },
                modifier = Modifier.background(SurfaceCard)
            ) {
                goalOptions.forEach { opt ->
                    DropdownMenuItem(
                        text = { Text(opt, color = TextPrimary) },
                        onClick = {
                            goal = opt
                            goalExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = {
                onSave(name, ageRange, expLevel, goal)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("profile_save_button"),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonEmerald,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = LanguageStrings.get("save_btn", isBangla),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
