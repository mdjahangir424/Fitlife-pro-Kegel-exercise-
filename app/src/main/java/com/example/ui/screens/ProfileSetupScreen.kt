package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.LanguageStrings
import kotlinx.coroutines.delay

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.IconButton

@Composable
fun ProfileSetupScreen(
    initialName: String,
    initialAge: String,
    initialExp: String,
    initialGoal: String,
    isBangla: Boolean,
    onSave: (name: String, age: String, exp: String, goal: String) -> Unit,
    onBack: (() -> Unit)? = null,
    onViewAboutOwner: (() -> Unit)? = null,
    currentUserEmail: String? = null
) {
    if (onBack != null) {
        BackHandler {
            onBack()
        }
    }

    var name by remember(initialName) {
        mutableStateOf(initialName.ifEmpty { if (isBangla) "মোঃ জাহাঙ্গীর আলম" else "Md Jahangir Alam" })
    }
    var ageRange by remember(initialAge) {
        mutableStateOf(initialAge.ifEmpty { "25 - 35" })
    }
    var expLevel by remember(initialExp) {
        mutableStateOf(initialExp.ifEmpty { if (isBangla) "শুরু করছি (Beginner)" else "Beginner" })
    }
    var goal by remember(initialGoal) {
        mutableStateOf(initialGoal.ifEmpty { if (isBangla) "পেলভিক পেশি শক্তি বৃদ্ধি" else "Pelvic Floor Strength" })
    }

    var ageExpanded by remember { mutableStateOf(false) }
    var expExpanded by remember { mutableStateOf(false) }
    var goalExpanded by remember { mutableStateOf(false) }
    var showSavedNotification by remember { mutableStateOf(false) }

    LaunchedEffect(showSavedNotification) {
        if (showSavedNotification) {
            delay(2500)
            showSavedNotification = false
        }
    }

    val ageOptions = listOf("18 - 24", "25 - 35", "36 - 45", "46+")
    val expOptions = if (isBangla) {
        listOf("শুরু করছি (Beginner)", "মধ্যবর্তী (Intermediate)", "অভিজ্ঞ (Advanced)")
    } else {
        listOf("Beginner", "Intermediate", "Advanced")
    }
    val goalOptions = if (isBangla) {
        listOf(
            "পেলভিক পেশি শক্তি বৃদ্ধি",
            "ব্লাডার ও কোর নিয়ন্ত্রণ",
            "শারীরিক সক্ষমতা ও স্ট্যামিনা",
            "রিল্যাক্সেশন ও নমনীয়তা"
        )
    } else {
        listOf(
            "Pelvic Floor Strength",
            "Bladder & Core Control",
            "Physical Stamina",
            "Flexibility & Relaxation"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Top Back bar if not in onboarding
        if (onBack != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onBack() },
                    modifier = Modifier.testTag("profile_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isBangla) "প্রোফাইল সেটিংস" else "Profile Settings",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        } else {
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Screen Header
        Text(
            text = LanguageStrings.get("profile_title", isBangla),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = LanguageStrings.get("profile_sub", isBangla),
            fontSize = 13.sp,
            color = TextMuted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Avatar & Cloud Sync Pill
        Box(
            modifier = Modifier
                .size(86.dp)
                .clip(CircleShape)
                .background(Color(0xFF0C2D48))
                .border(2.5.dp, SkyBluePrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                tint = SkyBlueLight,
                modifier = Modifier.size(50.dp)
            )
        }

        if (currentUserEmail != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF052E16))
                    .border(1.dp, NeonEmerald.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CloudDone,
                    contentDescription = "Cloud Synced",
                    tint = NeonEmerald,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = currentUserEmail,
                    fontSize = 11.sp,
                    color = NeonEmerald,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Success banner
        AnimatedVisibility(
            visible = showSavedNotification,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF052E16))
                    .border(1.5.dp, NeonEmerald, RoundedCornerShape(12.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = NeonEmerald,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = LanguageStrings.get("profile_save_success", isBangla),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Form Fields Container Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Name field
                Text(
                    text = LanguageStrings.get("name_label", isBangla),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary,
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
                        focusedBorderColor = SkyBluePrimary,
                        unfocusedBorderColor = SurfaceBorder,
                        focusedContainerColor = BackgroundDark,
                        unfocusedContainerColor = BackgroundDark
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Age bracket dropdown
                Text(
                    text = LanguageStrings.get("age_label", isBangla),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(BackgroundDark)
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

                Spacer(modifier = Modifier.height(16.dp))

                // Experience dropdown
                Text(
                    text = LanguageStrings.get("experience_label", isBangla),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(BackgroundDark)
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

                Spacer(modifier = Modifier.height(16.dp))

                // Goal dropdown
                Text(
                    text = LanguageStrings.get("goal_label", isBangla),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(BackgroundDark)
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
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Save Profile Button
        Button(
            onClick = {
                onSave(name, ageRange, expLevel, goal)
                showSavedNotification = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("profile_save_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SkyBluePrimary,
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = LanguageStrings.get("save_btn", isBangla),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }



        Spacer(modifier = Modifier.height(36.dp))
    }
}
