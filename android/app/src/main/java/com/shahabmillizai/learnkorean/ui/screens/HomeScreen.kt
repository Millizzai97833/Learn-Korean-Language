package com.shahabmillizai.learnkorean.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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

@Composable
fun HomeScreen(
    repository: KoreanRepository,
    ttsManager: KoreanTtsManager,
    rewardedManager: RewardedInterstitialAdManager,
    onNavigateToLesson: (String) -> Unit,
    onNavigateToPractice: () -> Unit
) {
    val context = LocalContext.current
    val progress by repository.userPreferences.progressFlow.collectAsState()
    val levels = remember { repository.getLevels() }
    var showRewardDialog by remember { mutableStateOf(false) }

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
            // Header stats: Streak & XP
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
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "By Shahab Millizai",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = TaegeukRed.copy(alpha = 0.12f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                TaegeukRed.copy(alpha = 0.3f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 6.dp
                                ),
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
                                    text = "${progress.streakDays}d Streak",
                                    color = TaegeukRed,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = TaegeukBlue.copy(alpha = 0.12f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                TaegeukBlue.copy(alpha = 0.3f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 6.dp
                                ),
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
                                    text = "${progress.xp} XP",
                                    color = TaegeukBlue,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            // Word of the Day
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant
                    )
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
                                text = "DAILY WORD OF THE DAY",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = TaegeukBlue
                            )

                            IconButton(
                                onClick = {
                                    ttsManager.speakKorean("안녕하세요")
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(TaegeukBlue.copy(alpha = 0.1f))
                            ) {
                                Icon(
                                    Icons.Default.VolumeUp,
                                    contentDescription = "Listen",
                                    tint = TaegeukBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Text(
                            text = "안녕하세요",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = "Annyeonghaseyo",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "English",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                                Text(
                                    text = "Hello / Peace be with you",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "اردو ترجمہ",
                                    fontSize = 11.sp,
                                    color = UrduEmerald
                                )
                                Text(
                                    text = "ہیلو / السلام علیکم",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = UrduEmerald
                                )
                            }
                        }
                    }
                }
            }

            // Hangul Trainer
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToPractice() },
                    shape = RoundedCornerShape(16.dp),
                    color = TaegeukRed.copy(alpha = 0.08f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        TaegeukRed.copy(alpha = 0.25f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(TaegeukRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "한",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Hangul Alphabet Trainer",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Practice consonants & vowels with audio",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }

                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = TaegeukRed
                        )
                    }
                }
            }

            // Rewarded Ad Bonus Pack
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.CardGiftcard,
                            contentDescription = null,
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
                                    "You have unlocked +150 XP and premium phrases."
                                } else {
                                    "Watch a short ad to earn +150 XP & bonus phrases."
                                },
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }

                        if (!progress.isBonusPackUnlocked) {
                            Button(
                                onClick = { showRewardDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TaegeukBlue
                                ),
                                contentPadding = PaddingValues(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                )
                            ) {
                                Text(
                                    text = "Unlock",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            // Curriculum heading
            item {
                Text(
                    text = "Core Curriculum (4 Levels)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Curriculum levels
            items(levels.size) { index ->
                val level = levels[index]

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onNavigateToLesson(
                                level.lessons.firstOrNull()?.id ?: "beg_01"
                            )
                        },
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant
                    )
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
                                text = level.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = level.koreanName,
                                color = TaegeukRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }

                        Text(
                            text = level.urduName,
                            color = UrduEmerald,
                            fontSize = 13.sp
                        )

                        Text(
                            text = level.description,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${level.lessons.size} structured lessons",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )

                            Text(
                                text = "Start Lesson →",
                                color = TaegeukBlue,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Start.io banner ad
        BannerAdView()
    }

    // Rewarded video confirmation
    if (showRewardDialog) {
        AlertDialog(
            onDismissRequest = {
                showRewardDialog = false
            },
            title = {
                Text(text = "Unlock Bonus Vocab Pack?")
            },
            text = {
                Text(
                    text = "Watch a sponsored video to unlock bonus TOPIK " +
                        "phrases and earn +150 XP. The reward is granted " +
                        "only after the video completes."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showRewardDialog = false

                        (context as? Activity)?.let { activity ->
                            rewardedManager.show(
                                activity = activity,
                                onRewardEarned = { _, _ ->
                                    repository.userPreferences.unlockBonusPack()
                                },
                                onAdClosed = {}
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TaegeukBlue
                    )
                ) {
                    Text("Watch Ad & Unlock")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRewardDialog = false
                    }
                ) {
                    Text("Maybe Later")
                }
            }
        )
    }
}
