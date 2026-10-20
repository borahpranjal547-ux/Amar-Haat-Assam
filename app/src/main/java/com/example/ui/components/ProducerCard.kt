package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SellerEntity
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.*

@Composable
fun ProducerCard(
    seller: SellerEntity,
    language: AppLanguage,
    onViewProducts: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val desc = if (language == AppLanguage.ASSAMESE) seller.descriptionAs else seller.descriptionEn

    val typeLabel = when (seller.sellerType) {
        "FARMER" -> if (language == AppLanguage.ASSAMESE) "ব্যক্তিগত কৃষক" else "Individual Farmer"
        "SHG" -> if (language == AppLanguage.ASSAMESE) "আত্মসহায়ক গোট (SHG)" else "Self-Help Group (SHG)"
        "FPO" -> if (language == AppLanguage.ASSAMESE) "কৃষক উৎপাদক সংগঠন (FPO)" else "Farmer Producer Org (FPO)"
        "ARTISAN" -> if (language == AppLanguage.ASSAMESE) "শিল্পী / কাৰিকৰ" else "Artisan / Craftsman"
        "LOCAL_MANUFACTURER" -> if (language == AppLanguage.ASSAMESE) "স্থানীয় খাদ্য উৎপাদক" else "Local Food Maker"
        else -> if (language == AppLanguage.ASSAMESE) "ক্ষুদ্ৰ উৎপাদক" else "Local Producer"
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("producer_card_${seller.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(AssamTeaLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (seller.sellerType) {
                            "FARMER" -> Icons.Default.Agriculture
                            "SHG" -> Icons.Default.Groups
                            "ARTISAN" -> Icons.Default.Brush
                            "FPO" -> Icons.Default.Business
                            else -> Icons.Default.Storefront
                        },
                        contentDescription = null,
                        tint = AssamForestGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = seller.farmOrBusinessName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AssamTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        VerificationBadge(
                            isVerified = seller.approvalStatus == "APPROVED",
                            language = language
                        )
                    }

                    Text(
                        text = "$typeLabel • ${seller.fullName}",
                        fontSize = 12.sp,
                        color = AssamTextSecondary
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = AssamGoldenPaddy,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${seller.village}, ${seller.district}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = AssamForestGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = desc,
                fontSize = 13.sp,
                color = AssamTextSecondary,
                lineHeight = 18.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onViewProducts,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.weight(1f).height(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "সামগ্ৰী চাওক" else "View Listings",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (seller.publicContactVisible) {
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${seller.mobileNumber}")
                            }
                            context.startActivity(intent)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = AssamTeaLight,
                            contentColor = AssamForestGreen
                        ),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Call Producer",
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            val msg = "Hello ${seller.farmOrBusinessName}, I saw your products on Amar Haat Assam."
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://wa.me/91${seller.whatsappNumber}?text=${Uri.encode(msg)}")
                            }
                            context.startActivity(intent)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFFDCFCE7),
                            contentColor = Color(0xFF16A34A)
                        ),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "WhatsApp",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
