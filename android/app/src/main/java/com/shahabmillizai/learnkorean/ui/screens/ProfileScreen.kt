package com.shahabmillizai.learnkorean.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.shahabmillizai.learnkorean.ads.BannerAdView
import com.shahabmillizai.learnkorean.ads.ConsentManager
import com.shahabmillizai.learnkorean.data.repository.KoreanRepository
import com.shahabmillizai.learnkorean.ui.theme.TaegeukBlue
import com.shahabmillizai.learnkorean.ui.theme.TaegeukRed
import com.shahabmillizai.learnkorean.ui.theme.UrduEmerald

@Composable
fun ProfileScreen(
    repository: KoreanRepository,
    consentManager: ConsentManager
) {
    val context = LocalContext.current
    val progress by repository.userPreferences.progressFlow.collectAsState()
    var showPrivacyDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.School,
                                contentDescription = "Learner",
                                tint = TaegeukBlue,
                                modifier = Modifier.size(42.dp)
                            )
                        }

                        Text(
                            "Korean Learner",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )

                        Text(
                            "Application by Shahab Millizai",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        HorizontalDivider()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "${progress.xp}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = TaegeukBlue
                                )
                                Text("Total XP", fontSize = 12.sp)
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "${progress.streakDays}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = TaegeukRed
                                )
                                Text("Streak days", fontSize = 12.sp)
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "${progress.completedLessonIds.size}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = UrduEmerald
                                )
                                Text("Completed", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    "Monetization",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Start.io Advertising",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "This app uses Start.io to display supported advertisements.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            "Start.io App ID: 210777317",
                            fontSize = 12.sp
                        )
                    }
                }
            }

            item {
                Text(
                    "Privacy & Policy",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Security,
                                contentDescription = null,
                                tint = TaegeukBlue
                            )

                            Text(
                                "Privacy Policy & Disclosures",
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Text(
                            "Learn Korean Language by Shahab Millizai uses " +
                                "Start.io advertising services. Advertising and " +
                                "consent practices are subject to the applicable " +
                                "Start.io policies and the app's published privacy policy.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        TextButton(
                            onClick = { showPrivacyDialog = true }
                        ) {
                            Text("Read privacy information")
                        }

                        OutlinedButton(
                            onClick = {
                                (context as? Activity)?.let { activity ->
                                    consentManager.showPrivacyOptionsForm(activity) {}
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Change Advertising Privacy Choices")
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            "About",
                            fontWeight = FontWeight.Bold
                        )

                        Text("Learn Korean Language")
                        Text(
                            "Developer: Shahab Millizai",
                            fontSize = 13.sp
                        )
                        Text(
                            "Package: com.shahabmillizai.learnkorean",
                            fontSize = 12.sp
                        )
                        Text(
                            "Learn Hangul, Korean vocabulary, grammar, " +
                                "pronunciation and TOPIK preparation with Urdu " +
                                "and English explanations.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        BannerAdView()
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = {
                Text("Privacy Information")
            },
            text = {
                Text(
                    "Learn Korean Language provides offline learning " +
                        "content and uses Start.io advertising services. " +
                        "Please read the app's published privacy policy " +
                        "and Start.io's privacy information for details " +
                        "about advertising and data practices."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { showPrivacyDialog = false }
                ) {
                    Text("Close")
                }
            }
        )
    }
}
