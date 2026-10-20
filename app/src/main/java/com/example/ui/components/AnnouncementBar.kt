package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AnnouncementEntity
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.AssamForestGreen
import com.example.ui.theme.AssamGoldenPaddy

@Composable
fun AnnouncementBar(
    announcements: List<AnnouncementEntity>,
    language: AppLanguage,
    onAnnouncementClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (announcements.isEmpty()) return

    var currentIndex by remember { mutableStateOf(0) }
    var isVisible by remember { mutableStateOf(true) }

    AnimatedVisibility(visible = isVisible) {
        val current = announcements[currentIndex % announcements.size]
        val text = if (language == AppLanguage.ASSAMESE) current.textAs else current.textEn

        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(Color(0xFFFBF4DC))
                .clickable {
                    if (announcements.size > 1) {
                        currentIndex = (currentIndex + 1) % announcements.size
                    }
                    onAnnouncementClick()
                }
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Campaign,
                contentDescription = "Announcement",
                tint = Color(0xFF7A5805),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = Color(0xFF3E2D02),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            if (announcements.size > 1) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Next announcement",
                    tint = Color(0xFF7A5805),
                    modifier = Modifier.size(16.dp)
                )
            }
            IconButton(
                onClick = { isVisible = false },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color(0xFF7A5805),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
