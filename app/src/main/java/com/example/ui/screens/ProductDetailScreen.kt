package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ProductEntity
import com.example.ui.components.AvailabilityBadge
import com.example.ui.components.OrganicBadge
import com.example.ui.components.VerificationBadge
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.*

@Composable
fun ProductDetailScreen(
    product: ProductEntity?,
    language: AppLanguage,
    isWishlisted: Boolean,
    onBackClick: () -> Unit,
    onSendEnquiry: (ProductEntity) -> Unit,
    onToggleWishlist: () -> Unit,
    onReportListing: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    if (product == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(if (language == AppLanguage.ASSAMESE) "সামগ্ৰী পোৱা নগ’ল" else "Product not found")
        }
        return
    }

    val name = if (language == AppLanguage.ASSAMESE) product.nameAs else product.nameEn
    val desc = if (language == AppLanguage.ASSAMESE) product.descriptionAs else product.descriptionEn
    val catName = if (language == AppLanguage.ASSAMESE) product.categoryNameAs else product.categoryNameEn

    Scaffold(
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Quick Phone Call
                    OutlinedIconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${product.sellerPhone}")
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Call Producer",
                            tint = AssamForestGreen
                        )
                    }

                    // Direct WhatsApp
                    if (product.sellerWhatsApp.isNotBlank()) {
                        IconButton(
                            onClick = {
                                val msg = "Hello ${product.sellerName}, I am interested in buying ${product.nameEn} on Amar Haat Assam."
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse("https://wa.me/91${product.sellerWhatsApp}?text=${Uri.encode(msg)}")
                                }
                                context.startActivity(intent)
                            },
                            colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFDCFCE7)),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "WhatsApp",
                                tint = Color(0xFF16A34A)
                            )
                        }
                    }

                    // Send Enquiry primary button
                    Button(
                        onClick = { onSendEnquiry(product) },
                        colors = ButtonDefaults.buttonColors(containerColor = AssamForestGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("detail_send_enquiry_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "অনুসন্ধান পঠিয়াওক" else "Send Enquiry",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AssamCreamBackground)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Hero Banner with Icons
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFFE8F5E9))
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .background(Color.White.copy(alpha = 0.9f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = AssamForestGreen
                    )
                }

                IconButton(
                    onClick = onToggleWishlist,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(Color.White.copy(alpha = 0.9f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Save",
                        tint = if (isWishlisted) Color.Red else Color.Gray
                    )
                }

                // Center Icon Illustration
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Agriculture,
                            contentDescription = null,
                            tint = AssamForestGreen,
                            modifier = Modifier.size(42.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = catName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AssamForestGreen
                    )
                }
            }

            // Main Info Card
            Card(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-16).dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Availability & Organic row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AvailabilityBadge(status = product.availabilityStatus, language = language)
                        if (product.isOrganicCertified) {
                            OrganicBadge(language = language)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AssamTextPrimary,
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Price display
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "₹${product.price.toInt()}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AssamForestGreen
                        )
                        Text(
                            text = " / ${product.unit}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = AssamTextSecondary,
                            modifier = Modifier.padding(bottom = 3.dp, start = 4.dp)
                        )
                    }

                    Text(
                        text = "${if (language == AppLanguage.ASSAMESE) "মুঠ মজুত: " else "Available: "}${product.availableQuantity.toInt()} ${product.unit} • ${if (language == AppLanguage.ASSAMESE) "নূন্যতম ক্ৰয়: " else "Min order: "}${product.minOrderQuantity.toInt()} ${product.unit}",
                        fontSize = 12.sp,
                        color = AssamTextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    // Producer Info Card
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "উৎপাদক আৰু ঠিকনা" else "Producer & Farm Location",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AssamTextPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = AssamSurfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(AssamTeaLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = AssamForestGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = product.sellerName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AssamTextPrimary
                                    )
                                    Text(
                                        text = "${product.sellerVillage}, ${product.sellerDistrict}",
                                        fontSize = 12.sp,
                                        color = AssamTextSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                if (product.pickupAvailable) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = AssamForestGreen,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = if (language == AppLanguage.ASSAMESE) " পোনপটীয়া সংগ্ৰহ" else " Farm Pickup",
                                            fontSize = 11.sp,
                                            color = AssamForestGreen,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                if (product.deliveryAvailable) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocalShipping,
                                            contentDescription = null,
                                            tint = AssamForestGreen,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = if (language == AppLanguage.ASSAMESE) " স্থানীয় ডেলিভাৰী" else " Local Delivery",
                                            fontSize = 11.sp,
                                            color = AssamForestGreen,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "সামগ্ৰীৰ বিৱৰণ" else "Product Description",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AssamTextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = desc,
                        fontSize = 13.sp,
                        color = AssamTextSecondary,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Status confirmation notice
                    Surface(
                        color = Color(0xFFF9FAFB),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = AssamGoldenPaddy,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${if (language == AppLanguage.ASSAMESE) "মজুতৰ নিশ্চিতি: " else "Availability last confirmed: "}${product.lastConfirmedDate}",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Report listing
                    TextButton(
                        onClick = onReportListing,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Flag,
                            contentDescription = "Report",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "ভুল তথ্যৰ বিষয়ে অভিযোগ জনাওক" else "Report incorrect information",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
