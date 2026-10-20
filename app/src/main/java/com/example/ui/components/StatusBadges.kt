package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.*

@Composable
fun AvailabilityBadge(
    status: String,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val (label, bgColor, textColor) = when (status) {
        "AVAILABLE_NOW" -> Triple(
            if (language == AppLanguage.ASSAMESE) "এতিয়া মজুত আছে" else "Available Now",
            Color(0xFFE8F5E9),
            AssamForestGreen
        )
        "FUTURE_HARVEST" -> Triple(
            if (language == AppLanguage.ASSAMESE) "ভৱিষ্যত চপোৱা" else "Future Harvest",
            Color(0xFFFFF3E0),
            Color(0xFFC05621)
        )
        "SEASONAL" -> Triple(
            if (language == AppLanguage.ASSAMESE) "ঋতুভিত্তিক" else "Seasonal",
            Color(0xFFE0F2FE),
            Color(0xFF0369A1)
        )
        else -> Triple(
            if (language == AppLanguage.ASSAMESE) "সাময়িকভাৱে নাই" else "Unavailable",
            Color(0xFFFFEBEE),
            Color(0xFFC62828)
        )
    }

    Box(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(textColor, CircleShape)
            )
            Text(
                text = " $label",
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun PilotZoneBadge(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color(0xFFFBF4DC), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = AssamGoldenPaddy,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = " $text",
                color = Color(0xFF7A5805),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun OrganicBadge(
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color(0xFFE6F4EA), RoundedCornerShape(8.dp))
            .padding(horizontal = 6.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Spa,
                contentDescription = null,
                tint = AssamForestGreen,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = if (language == AppLanguage.ASSAMESE) " জৈৱিক" else " Organic",
                color = AssamForestGreen,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun VerificationBadge(
    isVerified: Boolean,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    if (isVerified) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .background(Color(0xFFE8F5E9), RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Verified Producer",
                tint = AssamForestGreen,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = if (language == AppLanguage.ASSAMESE) " প্ৰমাণিত উৎপাদক" else " Verified",
                color = AssamForestGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
