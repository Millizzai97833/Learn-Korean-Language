package com.shahabmillizai.learnkorean.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shahabmillizai.learnkorean.ads.BannerAdView
import com.shahabmillizai.learnkorean.data.repository.KoreanRepository
import com.shahabmillizai.learnkorean.ui.theme.TaegeukBlue
import com.shahabmillizai.learnkorean.ui.theme.TaegeukRed
import com.shahabmillizai.learnkorean.ui.theme.UrduEmerald

@Composable
fun LessonsScreen(
    repository: KoreanRepository,
    onNavigateToLesson: (String) -> Unit
) {
    val levels = remember { repository.getLevels() }
    var selectedLevelIndex by remember { mutableStateOf(0) }
    val progress by repository.userPreferences.progressFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Korean Curriculum",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Structured learning path from Hangul to TOPIK",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Level Selector Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedLevelIndex,
                edgePadding = 0.dp,
                divider = {},
                containerColor = MaterialTheme.colorScheme.background
            ) {
                levels.forEachIndexed { index, level ->
                    Tab(
                        selected = selectedLevelIndex == index,
                        onClick = { selectedLevelIndex = index },
                        text = {
                            Text(
                                text = level.name,
                                fontWeight = if (selectedLevelIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedLevelIndex == index) TaegeukBlue else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                    )
                }
            }
        }

        val activeLevel = levels.getOrNull(selectedLevelIndex)

        // Lessons List for selected level
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (activeLevel != null) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = TaegeukBlue.copy(alpha = 0.08f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TaegeukBlue.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = activeLevel.koreanName, fontWeight = FontWeight.Bold, color = TaegeukRed)
                                Text(text = activeLevel.urduName, color = UrduEmerald, fontWeight = FontWeight.Medium)
                            }
                            Text(text = activeLevel.description, fontSize = 13.sp)
                        }
                    }
                }

                items(activeLevel.lessons) { lesson ->
                    val isCompleted = progress.completedLessonIds.contains(lesson.id)

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToLesson(lesson.id) },
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isCompleted) TaegeukBlue.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Icon(
                                imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.PlayCircleOutline,
                                contentDescription = null,
                                tint = if (isCompleted) TaegeukBlue else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                modifier = Modifier.size(28.dp)
                            )

                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(text = lesson.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = lesson.koreanTitle, fontSize = 13.sp, color = TaegeukRed)
                                    Text(text = lesson.urduTitle, fontSize = 12.sp, color = UrduEmerald)
                                }
                                Text(
                                    text = "${lesson.items.size} phrases · ${lesson.quiz.size} practice questions",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Banner Ad
        BannerAdView()
    }
}
