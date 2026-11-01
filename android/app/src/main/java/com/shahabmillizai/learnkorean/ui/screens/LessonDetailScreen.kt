package com.shahabmillizai.learnkorean.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.shahabmillizai.learnkorean.ads.InterstitialAdManager
import com.shahabmillizai.learnkorean.data.repository.KoreanRepository
import com.shahabmillizai.learnkorean.tts.KoreanTtsManager
import com.shahabmillizai.learnkorean.ui.theme.TaegeukBlue
import com.shahabmillizai.learnkorean.ui.theme.TaegeukRed
import com.shahabmillizai.learnkorean.ui.theme.UrduEmerald

@Composable
fun LessonDetailScreen(
    lessonId: String,
    repository: KoreanRepository,
    ttsManager: KoreanTtsManager,
    interstitialManager: InterstitialAdManager,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val lesson = remember(lessonId) { repository.getLessonById(lessonId) }
    var selectedTab by remember { mutableStateOf(0) } // 0: Vocabulary & Phrases, 1: Grammar, 2: Quiz
    var currentQuizIndex by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var isLessonCompleted by remember { mutableStateOf(false) }

    if (lesson == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Lesson not found")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App Bar
        TopAppBar(
            title = {
                Column {
                    Text(text = lesson.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = "${lesson.koreanTitle} · ${lesson.urduTitle}", style = MaterialTheme.typography.labelSmall, color = TaegeukRed)
                }
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
        )

        // Subtabs: Content, Grammar, Quiz
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = TaegeukBlue
        ) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Phrases (${lesson.items.size})") })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Grammar") })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Quiz (${lesson.quiz.size})") })
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (selectedTab) {
                0 -> {
                    // Phrases and Examples List
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        itemsIndexed(lesson.items) { index, item ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = item.korean, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TaegeukBlue)
                                        IconButton(
                                            onClick = { ttsManager.speakKorean(item.korean) },
                                            modifier = Modifier.size(36.dp).clip(CircleShape).background(TaegeukBlue.copy(alpha = 0.1f))
                                        ) {
                                            Icon(Icons.Default.VolumeUp, contentDescription = "Audio", tint = TaegeukBlue, modifier = Modifier.size(18.dp))
                                        }
                                    }

                                    Text(text = item.romanization, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

                                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(text = "English", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                                            Text(text = item.english, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(text = "اردو معنی", fontSize = 11.sp, color = UrduEmerald)
                                            Text(text = item.urdu, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = UrduEmerald)
                                        }
                                    }

                                    if (item.exampleKorean.isNotBlank()) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                                .padding(10.dp)
                                        ) {
                                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Text(text = "Example: ${item.exampleKorean}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                                Text(text = item.exampleEnglish, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                                Text(text = item.exampleUrdu, fontSize = 12.sp, color = UrduEmerald)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Button(
                                onClick = {
                                    isLessonCompleted = true
                                    repository.userPreferences.markLessonCompleted(lesson.id)
                                    // Trigger safe natural transition interstitial
                                    (context as? Activity)?.let { act ->
                                        interstitialManager.showIfAllowed(
                                            activity = act,
                                            isQuizActive = false,
                                            onDismissed = { onNavigateBack() }
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = TaegeukBlue),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(text = "Mark Lesson Complete (+50 XP)", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                1 -> {
                    // Grammar Explanations (English & Urdu)
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                            ) {
                                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text(text = "Grammar Breakdown (English)", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TaegeukBlue)
                                    Text(text = lesson.grammarNotes, lineHeight = 22.sp, fontSize = 14.sp)

                                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                                    Text(text = "گرامر کی وضاحت (اردو میں)", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = UrduEmerald)
                                    Text(text = lesson.urduExplanation, lineHeight = 24.sp, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Interactive Quiz Screen
                    if (lesson.quiz.isNotEmpty()) {
                        val q = lesson.quiz[currentQuizIndex.coerceIn(0, lesson.quiz.size - 1)]
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Question ${currentQuizIndex + 1} of ${lesson.quiz.size}",
                                style = MaterialTheme.typography.labelMedium,
                                color = TaegeukBlue
                            )
                            Text(text = q.question, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(text = q.urduQuestion, fontSize = 14.sp, color = UrduEmerald)

                            Spacer(modifier = Modifier.height(8.dp))

                            q.options.forEachIndexed { optIndex, optionText ->
                                val isSelected = selectedOption == optIndex
                                val isCorrect = showResult && optIndex == q.correctIndex
                                val isWrong = showResult && isSelected && optIndex != q.correctIndex

                                val bgColor = when {
                                    isCorrect -> Color(0xFF10B981).copy(alpha = 0.15f)
                                    isWrong -> TaegeukRed.copy(alpha = 0.15f)
                                    isSelected -> TaegeukBlue.copy(alpha = 0.1f)
                                    else -> MaterialTheme.colorScheme.surface
                                }

                                val borderColor = when {
                                    isCorrect -> Color(0xFF10B981)
                                    isWrong -> TaegeukRed
                                    isSelected -> TaegeukBlue
                                    else -> MaterialTheme.colorScheme.outlineVariant
                                }

                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(enabled = !showResult) { selectedOption = optIndex },
                                    shape = RoundedCornerShape(12.dp),
                                    color = bgColor,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = optionText, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                                        if (isCorrect) Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF10B981))
                                        if (isWrong) Icon(Icons.Default.Close, contentDescription = null, tint = TaegeukRed)
                                    }
                                }
                            }

                            if (showResult) {
                                Text(
                                    text = "Explanation: ${q.explanation}",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            if (!showResult) {
                                Button(
                                    onClick = {
                                        if (selectedOption != null) {
                                            showResult = true
                                            if (selectedOption == q.correctIndex) {
                                                repository.userPreferences.addXp(20)
                                            }
                                        }
                                    },
                                    enabled = selectedOption != null,
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = TaegeukBlue)
                                ) {
                                    Text("Check Answer")
                                }
                            } else {
                                Button(
                                    onClick = {
                                        if (currentQuizIndex + 1 < lesson.quiz.size) {
                                            currentQuizIndex++
                                            selectedOption = null
                                            showResult = false
                                        } else {
                                            // Finish quiz, add reward & transition
                                            isLessonCompleted = true
                                            repository.userPreferences.markLessonCompleted(lesson.id)
                                            onNavigateBack()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = TaegeukBlue)
                                ) {
                                    Text(if (currentQuizIndex + 1 < lesson.quiz.size) "Next Question" else "Complete Quiz (+30 XP)")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
