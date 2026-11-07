
package com.shahabmillizai.learnkorean.ui.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shahabmillizai.learnkorean.ads.BannerAdView
import com.shahabmillizai.learnkorean.ads.RewardedInterstitialAdManager
import com.shahabmillizai.learnkorean.data.repository.KoreanRepository
import com.shahabmillizai.learnkorean.tts.KoreanTtsManager
import com.shahabmillizai.learnkorean.ui.theme.TaegeukBlue
import com.shahabmillizai.learnkorean.ui.theme.TaegeukRed
import com.shahabmillizai.learnkorean.ui.theme.UrduEmerald

private tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}

@Composable
fun HomeScreen(
    repository: KoreanRepository,
    ttsManager: KoreanTtsManager,
    rewardedManager: RewardedInterstitialAdManager,
    onNavigateToLesson: (String) -> Unit,
    onNavigateToPractice: () -> Unit
) {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }

    val progress by repository.userPreferences.progressFlow.collectAsState()
    val levels = remember { repository.getLevels() }

    var showRewardDialog by remember { mutableStateOf(false) }
    var showRewardMessage by remember { mutableStateOf(false) }
    var rewardMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Learn Korean",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "By Shahab Millizai",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.LocalFireDepartment,
                                contentDescription = "Streak",
                                tint = TaegeukRed,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                "${progress.streakDays}d Streak",
                                color = TaegeukRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.Bolt,
                                contentDescription = "XP",
                                tint = TaegeukBlue,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                "${progress.xp} XP",
                                color = TaegeukBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "DAILY WORD OF THE DAY",
                                color = TaegeukBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )

                            IconButton(
                                onClick = {
                                    ttsManager.speakKorean("안녕하세요")
                                }
                            ) {
                                Icon(
                                    Icons.Default.VolumeUp,
                                    contentDescription = "Listen",
                                    tint = TaegeukBlue
                                )
                            }
                        }

                        Text(
                            "안녕하세요",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Annyeonghaseyo",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        HorizontalDivider()

                        Text(
                            "English: Hello / Peace be with you",
                            fontSize = 14.sp
                        )

                        Text(
                            "اردو: ہیلو / السلام علیکم",
                            color = UrduEmerald,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToPractice() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = TaegeukRed.copy(alpha = 0.08f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(TaegeukRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "한",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Hangul Alphabet Trainer",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Practice Korean consonants and vowels",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = "Open practice"
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.CardGiftcard,
                            contentDescription = "Bonus reward",
                            tint = TaegeukBlue,
                            modifier = Modifier.size(32.dp)
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (progress.isBonusPackUnlocked) {
                                    "Bonus TOPIK Pack Unlocked!"
                                } else {
                                    "Bonus TOPIK Vocabulary Pack"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )

                            Text(
                                text = if (progress.isBonusPackUnlocked) {
                                    "You unlocked the bonus pack and earned 150 XP."
                                } else {
                                    "Watch a rewarded video to earn 150 XP and bonus phrases."
                                },
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (!progress.isBonusPackUnlocked) {
                            Button(
                                onClick = {
                                    showRewardDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TaegeukBlue
                                )
                            ) {
                                Text("Unlock")
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    "Core Curriculum (4 Levels)",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(levels) { level ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onNavigateToLesson(
                                level.lessons.firstOrNull()?.id ?: "beg_01"
                            )
                        },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                level.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            Text(
                                level.koreanName,
                                color = TaegeukRed,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            level.urduName,
                            color = UrduEmerald,
                            fontSize = 13.sp
                        )

                        Text(
                            level.description,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "${level.lessons.size} structured lessons",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                "Start Lesson →",
                                color = TaegeukBlue,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Start.io banner ad
        BannerAdView()
    }

    // Rewarded video confirmation dialog
    if (showRewardDialog) {
        AlertDialog(
            onDismissRequest = {
                showRewardDialog = false
            },
            title = {
                Text("Unlock Bonus Vocabulary Pack")
            },
            text = {
                Text(
                    "Watch a rewarded video to unlock 150 XP " +
                        "and bonus TOPIK vocabulary."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showRewardDialog = false

                        if (activity == null) {
                            rewardMessage =
                                "Unable to open the ad. Please try again."
                            showRewardMessage = true
                        } else {
                            rewardedManager.show(
                                activity = activity,
                                onRewardEarned = { _, amount ->
                                    repository.userPreferences.unlockBonusPack()

                                    rewardMessage =
                                        "Congratulations! You earned $amount XP and unlocked the bonus pack."

                                    showRewardMessage = true
                                },
                                onAdClosed = {
                                    Log.i(
                                        "HomeScreen",
                                        "Rewarded ad closed"
                                    )
                                },
                                onAdNotReady = {
                                    rewardMessage =
                                        "No rewarded ad is ready right now. Please try again later."
                                    showRewardMessage = true
                                }
                            )
                        }
                    }
                ) {
                    Text("Watch Ad")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRewardDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Reward result dialog
    if (showRewardMessage) {
        AlertDialog(
            onDismissRequest = {
                showRewardMessage = false
            },
            title = {
                Text("Reward Status")
            },
            text = {
                Text(rewardMessage)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRewardMessage = false
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}
