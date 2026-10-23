package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.credentials.CredentialManager
import com.example.ui.auth.signOutUser
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.LanguageStrings

@Composable
fun AboutOwnerScreen(
    isBangla: Boolean,
    exportedJson: String,
    onGenerateExport: () -> Unit,
    onChangeLanguageClick: () -> Unit,
    onBack: () -> Unit,
    onSignOut: () -> Unit = {}
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }
    val currentUser = Firebase.auth.currentUser

    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Top App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SurfaceCard)
                    .testTag("about_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = LanguageStrings.get("about_owner", isBangla),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // App Logo & Version
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(SurfaceCard)
                    .border(2.dp, NeonEmerald, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fitlife_skyblue_icon_1792774391534),
                    contentDescription = "FitLife Pro Logo",
                    modifier = Modifier.size(76.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "FitLife Pro",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )

            Text(
                text = "v1.0.0 • Production Release Ready",
                fontSize = 12.sp,
                color = NeonEmerald,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Owner Information Card (Specified by user)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NeonEmerald.copy(alpha = 0.6f), RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0C243B))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified Owner",
                        tint = NeonEmerald,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = LanguageStrings.get("app_owner_label", isBangla),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonEmerald
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Owner Name
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Owner", tint = TextMuted, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = LanguageStrings.get("owner_name", isBangla),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Email with click-to-compose
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:mdjahangiralam2528@gmail.com")
                                putExtra(Intent.EXTRA_SUBJECT, "FitLife Pro App Inquiry")
                            }
                            context.startActivity(Intent.createChooser(intent, "Send Email"))
                        }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "Email", tint = AccentCyan, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "mdjahangiralam2528@gmail.com",
                        fontSize = 14.sp,
                        color = AccentCyan,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Phone with click-to-dial
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:+8801778600028")
                            }
                            context.startActivity(intent)
                        }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = "Phone", tint = NeonEmerald, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "+8801778600028",
                        fontSize = 14.sp,
                        color = NeonEmerald,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Database Ownership & Control Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Storage, contentDescription = "Storage", tint = NeonEmerald, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = LanguageStrings.get("database_control", isBangla),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = LanguageStrings.get("database_control_desc", isBangla),
                    fontSize = 13.sp,
                    color = TextMuted,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        onGenerateExport()
                        showExportDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("export_database_btn"),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0C4A6E),
                        contentColor = NeonEmerald
                    )
                ) {
                    Icon(imageVector = Icons.Default.Code, contentDescription = "Code", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = LanguageStrings.get("export_db", isBangla),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Firebase Cloud Account & Sync Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NeonEmerald.copy(alpha = 0.5f), RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CloudDone,
                            contentDescription = "Cloud Synced",
                            tint = NeonEmerald,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBangla) "ফায়ারবেস ক্লাউড অ্যাকাউন্ট" else "Firebase Cloud Account",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonEmerald
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF0C4A6E))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (isBangla) "রিয়েলটাইম সিঙ্ক" else "Live Synced",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonEmerald
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = currentUser?.email ?: currentUser?.displayName ?: "Google User",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )

                Text(
                    text = if (isBangla) "কেগেল এক্সারসাইজ ও ওয়ার্কআউট হিস্ট্রি ক্লাউডে সংরক্ষিত" else "Kegel exercises & history secured in Firestore",
                    fontSize = 11.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedButton(
                    onClick = {
                        signOutUser(
                            context = context,
                            credentialManager = credentialManager,
                            onSignOutComplete = onSignOut,
                            scope = scope
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("sign_out_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFFF5252)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Sign Out",
                        tint = Color(0xFFFF5252),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBangla) "অ্যাকাউন্ট থেকে সাইন আউট করুন" else "Sign Out of Account",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFF5252)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Actions List (Language, Privacy Policy, Help & Support)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard)
        ) {
            Column {
                // Change Language row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onChangeLanguageClick() }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Language, contentDescription = "Language", tint = NeonEmerald, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = LanguageStrings.get("change_language", isBangla),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next", tint = TextMuted)
                }

                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(SurfaceBorder))

                // Privacy Policy row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showPrivacyDialog = true }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.PrivacyTip, contentDescription = "Privacy", tint = Color(0xFF00B0FF), modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = LanguageStrings.get("privacy_policy", isBangla),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next", tint = TextMuted)
                }

                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(SurfaceBorder))

                // Help & Support row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showHelpDialog = true }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Help, contentDescription = "Help", tint = Color(0xFFFF9100), modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = LanguageStrings.get("help_support", isBangla),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next", tint = TextMuted)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Play Store Ready & Copyright Footer
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF273140))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = LanguageStrings.get("play_store_ready", isBangla),
                    fontSize = 11.sp,
                    color = NeonEmerald,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = LanguageStrings.get("made_with", isBangla),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = LanguageStrings.get("copyright", isBangla),
                fontSize = 11.sp,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    // Privacy Policy Dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text(LanguageStrings.get("privacy_policy", isBangla), color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = if (isBangla) {
                        "FitLife Pro অ্যাপটি আপনার ব্যক্তিগত গোপনীয়তাকে শতভাগ সম্মান করে। আপনার সমস্ত ফিটনেস তথ্য ও ডেটাবেস আপনার নিজস্ব ডিভাইসে সুরক্ষিত থাকে। কোন ব্যক্তিগত তথ্য বাইরের সার্ভারে আপলোড করা হয় না। মালিক: মোঃ জাহাঙ্গীর আলম।"
                    } else {
                        "FitLife Pro values your privacy. All your workout records, routines, and profile data are stored 100% on your device in a local Room database without external tracking. Creator & Owner: Md Jahangir Alam."
                    },
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text("OK", color = NeonEmerald, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = SurfaceCard
        )
    }

    // Help & Support Dialog
    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text(LanguageStrings.get("help_support", isBangla), color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = if (isBangla) "যেকোনো সাহায্য, মতামত বা ফিডব্যাকের জন্য যোগাযোগ করুন:" else "For assistance, customization, or inquiries:",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Email: mdjahangiralam2528@gmail.com", color = NeonEmerald, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Phone: +8801778600028", color = AccentCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text("OK", color = NeonEmerald, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = SurfaceCard
        )
    }

    // Database Export Dialog
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text(LanguageStrings.get("export_success", isBangla), color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.height(260.dp).verticalScroll(rememberScrollState())) {
                    Text(
                        text = if (isBangla) "আপনার ডিভাইসের সম্পূর্ণ ডেটাবেস ব্যাকআপ তৈরি হয়েছে (JSON format):" else "Full local database backup ready in JSON format:",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0D1117))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = exportedJson.ifEmpty { "Generating..." },
                            color = NeonEmerald,
                            fontSize = 11.sp,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("Close", color = NeonEmerald, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = SurfaceCard
        )
    }
}
