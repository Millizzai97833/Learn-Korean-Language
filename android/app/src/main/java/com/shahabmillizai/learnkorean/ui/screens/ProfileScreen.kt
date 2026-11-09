
package com.shahabmillizai.learnkorean.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shahabmillizai.learnkorean.ads.AdConfig
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

    val progress by repository.userPreferences
        .progressFlow.collectAsState()

    var showPrivacyDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(16.dp)
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
                        horizontalAlignment =
                            Alignment.CenterHorizontally,
                        verticalArrangement =
                            Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            Icons.Default.School,
                            contentDescription = "Learner",
                            tint = TaegeukBlue,
                            modifier = Modifier.size(48.dp)
                        )

                        Text(
                            "Korean Learner",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )

                        Text(
                            "Application by Shahab Millizai",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme
                                .onSurfaceVariant
                        )

                        HorizontalDivider()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.SpaceEvenly
                        ) {
                            Column(
                                horizontalAlignment =
                                    Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "${progress.xp}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = TaegeukBlue
                                )
                                Text("Total XP", fontSize = 12.sp)
                            }

                            Column(
                                horizontalAlignment =
                                    Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "${progress.streakDays}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = TaegeukRed
                                )
                                Text("Streak days", fontSize = 12.sp)
                            }

                            Column(
                                horizontalAlignment =
                                    Alignment.CenterHorizontally
                            ) {
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
                    "Advertising",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "AdMob Mediation",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "This app uses Google Mobile Ads. " +
                                "Configured mediation partners, " +
                                "including Start.io, may participate " +
                                "in ad requests.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme
                                .onSurfaceVariant
                        )

                        Text(
                            "Start.io App ID: ${AdConfig.START_IO_APP_ID}",
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
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically,
                            horizontalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Security,
                                contentDescription = null,
                                tint = TaegeukBlue
                            )

                            Text(
                                "Privacy Information",
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Text(
                            "Learn Korean Language provides " +
                                "offline learning content and uses " +
                                "Google Mobile Ads with configured " +
                                "advertising mediation partners. " +
                                "Ad availability and personalization " +
                                "depend on consent, region, and " +
                                "advertising inventory.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme
                                .onSurfaceVariant
                        )

                        TextButton(
                            onClick = {
                                showPrivacyDialog = true
                            }
                        ) {
                            Text("Read privacy information")
                        }

                        if (
                            consentManager.isPrivacyOptionsRequired
                        ) {
                            OutlinedButton(
                                onClick = {
                                    (context as? Activity)?.let {
                                        activity ->
                                        consentManager
                                            .showPrivacyOptionsForm(
                                                activity
                                            ) {}
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "Change Advertising Privacy Choices"
                                )
                            }
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
                        verticalArrangement =
                            Arrangement.spacedBy(6.dp)
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
                            "Learn Hangul, Korean vocabulary, " +
                                "grammar, pronunciation and TOPIK " +
                                "preparation with Urdu and English " +
                                "explanations.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme
                                .onSurfaceVariant
                        )
                    }
                }
            }
        }

        BannerAdView()
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = {
                showPrivacyDialog = false
            },
            title = {
                Text("Privacy Information")
            },
            text = {
                Text(
                    "This app uses Google Mobile Ads and " +
                        "configured advertising mediation partners. " +
                        "Please read the published privacy policy " +
                        "for complete information about data " +
                        "collection, advertising, and consent."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPrivacyDialog = false
                    }
                ) {
                    Text("Close")
                }
            }
        )
    }
}
