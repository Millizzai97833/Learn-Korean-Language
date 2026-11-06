
package com.shahabmillizai.learnkorean.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.shahabmillizai.learnkorean.tts.KoreanTtsManager
import com.shahabmillizai.learnkorean.ui.theme.TaegeukBlue
import com.shahabmillizai.learnkorean.ui.theme.TaegeukRed
import com.shahabmillizai.learnkorean.ui.theme.UrduEmerald

@Composable
fun DictionaryScreen(
    repository: KoreanRepository,
    ttsManager: KoreanTtsManager
) {
    var searchQuery by remember { mutableStateOf("") }

    val progress by repository.userPreferences.progressFlow.collectAsState()

    val searchResults = remember(
        searchQuery,
        progress.bookmarkedWords
    ) {
        repository.searchDictionary(searchQuery)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // Dictionary heading and search field
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = "Korean Dictionary",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Search Hangul, English, or اردو...")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                searchQuery = ""
                            }
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Text(
                text = "Showing ${searchResults.size} words",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.5f
                )
            )
        }

        // Dictionary word list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(searchResults) { item ->

                val isBookmarked =
                    progress.bookmarkedWords.contains(item.korean)

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant
                    )
                ) {

                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        // Korean pronunciation button
                        IconButton(
                            onClick = {
                                ttsManager.speakKorean(item.korean)
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(
                                    TaegeukBlue.copy(alpha = 0.1f)
                                )
                        ) {
                            Icon(
                                Icons.Default.VolumeUp,
                                contentDescription = "Audio",
                                tint = TaegeukBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Korean and translation details
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = item.korean,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = TaegeukBlue
                                )

                                Text(
                                    text = item.category,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.5f
                                    )
                                )
                            }

                            Text(
                                text = item.romanization,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.6f
                                )
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = item.english,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )

                                Text(
                                    text = item.urdu,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = UrduEmerald
                                )
                            }
                        }

                        // Bookmark button
                        IconButton(
                            onClick = {
                                repository.userPreferences.toggleBookmark(
                                    item.korean
                                )
                            }
                        ) {

                            Icon(
                                imageVector = if (isBookmarked) {
                                    Icons.Default.Bookmark
                                } else {
                                    Icons.Default.BookmarkBorder
                                },
                                contentDescription = "Bookmark",
                                tint = if (isBookmarked) {
                                    TaegeukRed
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.4f
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        // Start.io banner advertisement
        BannerAdView()
    }
}
