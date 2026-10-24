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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AppThemeMode
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

@Composable
fun LanguageScreen(
    currentLanguage: String,
    themeMode: AppThemeMode = AppThemeMode.DARK,
    onLanguageSelected: (String) -> Unit,
    onToggleTheme: ((AppThemeMode) -> Unit)? = null,
    onContinue: () -> Unit = {},
    onGoToProfile: (() -> Unit)? = null,
    isTabMode: Boolean = false
) {
    val isBangla = currentLanguage == "bn"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(28.dp))

        // Screen Icon & Header
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(Color(0xFF0C2D48))
                .border(2.dp, SkyBluePrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Translate,
                contentDescription = "Language",
                tint = SkyBlueLight,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isTabMode) LanguageStrings.get("language_tab_title", isBangla)
            else LanguageStrings.get("select_language", isBangla),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (isTabMode) LanguageStrings.get("language_tab_sub", isBangla)
            else LanguageStrings.get("select_language_sub", isBangla),
            fontSize = 13.sp,
            color = TextMuted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 1-Click Language Switch Card (Prominent Quick Toggle)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    val next = if (currentLanguage == "bn") "en" else "bn"
                    onLanguageSelected(next)
                }
                .testTag("quick_one_click_toggle_card"),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0C4A6E)),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(2.dp, SkyBluePrimary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(SkyBluePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SyncAlt,
                            contentDescription = "Toggle",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = LanguageStrings.get("quick_toggle_lang", isBangla),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isBangla) "বর্তমান: 🇧🇩 বাংলা (এক ক্লিকে ইংলিশ করুন)"
                            else "Current: 🇬🇧 English (Click for Bangla)",
                            fontSize = 12.sp,
                            color = SkyBlueLight
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (isBangla) "EN" else "বাংলা",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0C4A6E)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Bangla Option Card
        val isBnSelected = currentLanguage == "bn"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(if (isBnSelected) Color(0xFF082F49) else SurfaceCard)
                .border(
                    width = if (isBnSelected) 2.dp else 1.dp,
                    color = if (isBnSelected) SkyBluePrimary else SurfaceBorder,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onLanguageSelected("bn") }
                .padding(18.dp)
                .testTag("lang_bangla_option"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🇧🇩", fontSize = 32.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "বাংলা (Bangla)",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isBnSelected) SkyBlueLight else TextPrimary
                    )
                    Text(
                        text = "পেলভিক ফ্লোর ও ক্যাগেল সুস্থতা",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }
            if (isBnSelected) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(SkyBluePrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // English Option Card
        val isEnSelected = currentLanguage == "en"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(if (isEnSelected) Color(0xFF082F49) else SurfaceCard)
                .border(
                    width = if (isEnSelected) 2.dp else 1.dp,
                    color = if (isEnSelected) SkyBluePrimary else SurfaceBorder,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onLanguageSelected("en") }
                .padding(18.dp)
                .testTag("lang_english_option"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🇬🇧", fontSize = 32.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "English",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isEnSelected) SkyBlueLight else TextPrimary
                    )
                    Text(
                        text = "Pelvic Floor & Kegel Fitness",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }
            if (isEnSelected) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(SkyBluePrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Display Theme Mode Section (Dark, Light, System)
        Text(
            text = LanguageStrings.get("theme_select_title", isBangla),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Dark Mode Option
                ThemeModeItem(
                    title = LanguageStrings.get("theme_dark_mode", isBangla),
                    subtitle = if (isBangla) "রাত ও চোখের সুরক্ষার জন্য" else "Optimized for dark environments",
                    icon = Icons.Default.DarkMode,
                    isSelected = themeMode == AppThemeMode.DARK,
                    onClick = { onToggleTheme?.invoke(AppThemeMode.DARK) },
                    testTag = "theme_dark_option"
                )

                // Light Mode Option
                ThemeModeItem(
                    title = LanguageStrings.get("theme_light_mode", isBangla),
                    subtitle = if (isBangla) "উজ্জ্বল দিনের আলোর জন্য" else "Bright and crisp appearance",
                    icon = Icons.Default.LightMode,
                    isSelected = themeMode == AppThemeMode.LIGHT,
                    onClick = { onToggleTheme?.invoke(AppThemeMode.LIGHT) },
                    testTag = "theme_light_option"
                )

                // System Default Option
                ThemeModeItem(
                    title = LanguageStrings.get("theme_system_mode", isBangla),
                    subtitle = if (isBangla) "ফোনের সিস্টেম সেটিংস অনুসরণ করবে" else "Follow device system setting",
                    icon = Icons.Default.SettingsSuggest,
                    isSelected = themeMode == AppThemeMode.SYSTEM,
                    onClick = { onToggleTheme?.invoke(AppThemeMode.SYSTEM) },
                    testTag = "theme_system_option"
                )
            }
        }

        // Quick Navigation to Profile Setup tab
        if (isTabMode) {
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onGoToProfile?.invoke() }
                    .testTag("language_to_profile_tab"),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0C2D48)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = SkyBlueLight,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = LanguageStrings.get("go_to_profile_setup", isBangla),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = if (isBangla) "নাম, বয়স, ফিটনেস লক্ষ্য পরিবর্তন করুন" else "Update name, age, and goals",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Arrow",
                        tint = TextMuted
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("language_continue_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SkyBluePrimary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = LanguageStrings.get("continue_btn", isBangla),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))
    }
}

@Composable
private fun ThemeModeItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color(0xFF082F49) else BackgroundDark)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) SkyBluePrimary else SurfaceBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(14.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) SkyBlueLight else TextMuted,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) SkyBlueLight else TextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(SkyBluePrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
