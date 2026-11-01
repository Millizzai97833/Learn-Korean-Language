package com.shahabmillizai.learnkorean.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shahabmillizai.learnkorean.ads.BannerAdView
import com.shahabmillizai.learnkorean.data.repository.KoreanRepository
import com.shahabmillizai.learnkorean.tts.KoreanTtsManager
import com.shahabmillizai.learnkorean.ui.theme.TaegeukBlue
import com.shahabmillizai.learnkorean.ui.theme.TaegeukRed
import com.shahabmillizai.learnkorean.ui.theme.UrduEmerald

@Composable
fun PracticeScreen(
    repository: KoreanRepository,
    ttsManager: KoreanTtsManager
) {
    var practiceMode by remember { mutableStateOf(0) } // 0: Hangul Trainer, 1: Flashcards, 2: Word Match, 3: TOPIK
    val hangulList = remember { repository.getHangulAlphabet() }
    val dictItems = remember { repository.getDictionary() }

    // Flashcard state
    var cardIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Mode Selector Bar
        ScrollableTabRow(
            selectedTabIndex = practiceMode,
            edgePadding = 12.dp,
            divider = {},
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Tab(selected = practiceMode == 0, onClick = { practiceMode = 0 }, text = { Text("Hangul Trainer") })
            Tab(selected = practiceMode == 1, onClick = { practiceMode = 1 }, text = { Text("Flashcards") })
            Tab(selected = practiceMode == 2, onClick = { practiceMode = 2 }, text = { Text("Word Match") })
            Tab(selected = practiceMode == 3, onClick = { practiceMode = 3 }, text = { Text("TOPIK Mock") })
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            when (practiceMode) {
                0 -> {
                    // Hangul Alphabet Trainer Grid
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Tap any Hangul character to hear native audio and Urdu / English sound:",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(hangulList) { item ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            ttsManager.speakKorean(item.char)
                                        },
                                    shape = RoundedCornerShape(14.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(text = item.char, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TaegeukBlue)
                                        Text(text = item.name, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                        Text(text = item.sound, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                        Text(text = item.urduSound, fontSize = 12.sp, color = UrduEmerald, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    // Interactive Flip Flashcards
                    if (dictItems.isNotEmpty()) {
                        val current = dictItems[cardIndex % dictItems.size]
                        val rotation by animateFloatAsState(
                            targetValue = if (isFlipped) 180f else 0f,
                            animationSpec = tween(400),
                            label = "cardFlip"
                        )

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Card ${cardIndex + 1} of ${dictItems.size} (Tap to Flip)",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // 3D Flippable Card
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(260.dp)
                                    .graphicsLayer {
                                        rotationY = rotation
                                        cameraDistance = 12f * density
                                    }
                                    .clickable { isFlipped = !isFlipped },
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, TaegeukBlue.copy(alpha = 0.4f)),
                                shadowElevation = 4.dp
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize().padding(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (rotation <= 90f) {
                                        // Front: Korean + Audio
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Text(text = current.korean, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = TaegeukBlue)
                                            Text(text = current.romanization, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                            IconButton(
                                                onClick = { ttsManager.speakKorean(current.korean) },
                                                modifier = Modifier.size(42.dp).clip(CircleShape).background(TaegeukBlue.copy(alpha = 0.1f))
                                            ) {
                                                Icon(Icons.Default.VolumeUp, contentDescription = "Audio", tint = TaegeukBlue)
                                            }
                                        }
                                    } else {
                                        // Back: English + Urdu
                                        Column(
                                            modifier = Modifier.graphicsLayer { rotationY = 180f },
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Text(text = current.english, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                            Text(text = current.urdu, fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = UrduEmerald)
                                            Text(text = "Category: ${current.category}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        isFlipped = false
                                        cardIndex = if (cardIndex > 0) cardIndex - 1 else dictItems.size - 1
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Previous")
                                }

                                Button(
                                    onClick = {
                                        isFlipped = false
                                        cardIndex = (cardIndex + 1) % dictItems.size
                                        repository.userPreferences.addXp(10)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = TaegeukBlue),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Next (+10 XP)")
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Word Match Game
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Match Korean words with their Urdu/English translations:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )

                        val words = listOf("물" to "Water (پانی)", "밥" to "Meal / Rice (کھانا)", "친구" to "Friend (دوست)", "선생님" to "Teacher (استاد)")
                        words.forEach { (kor, eng) ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = kor, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TaegeukBlue)
                                    Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
                                    Text(text = eng, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }
                3 -> {
                    // TOPIK Mock Practice
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = TaegeukRed.copy(alpha = 0.08f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TaegeukRed.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(text = "TOPIK I / II Preparation", fontWeight = FontWeight.Bold, color = TaegeukRed)
                                Text(
                                    text = "Practice real exam patterns for Listening (듣기) and Reading (읽기).",
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Text(text = "[Sample Question 1] 빈칸에 들어갈 가장 알맞은 것을 고르십시오.", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "가: 주말에 보통 뭐 해요?\n나: 저는 집에서 영화를 (           ).", fontSize = 14.sp, lineHeight = 20.sp)

                        listOf("봅니다", "먹습니다", "갑니다", "잡니다").forEachIndexed { idx, opt ->
                            Surface(
                                modifier = Modifier.fillMaxWidth().clickable {},
                                shape = RoundedCornerShape(10.dp),
                                color = if (idx == 0) TaegeukBlue.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (idx == 0) TaegeukBlue else MaterialTheme.colorScheme.outlineVariant)
                            ) {
                                Text(
                                    text = "${idx + 1}. $opt ${if (idx == 0) "✓ (Correct: watch/see)" else ""}",
                                    modifier = Modifier.padding(12.dp),
                                    fontWeight = if (idx == 0) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }

        BannerAdView()
    }
}
