package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.EnquiryEntity
import com.example.data.local.ProductEntity
import com.example.ui.components.ProductCard
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.*

@Composable
fun CustomerHubScreen(
    wishlistedProducts: List<ProductEntity>,
    buyerEnquiries: List<EnquiryEntity>,
    wishlistIds: List<String>,
    language: AppLanguage,
    onProductClick: (String) -> Unit,
    onContactSeller: (ProductEntity) -> Unit,
    onToggleWishlist: (String) -> Unit,
    onSwitchToSellerMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Wishlist, 1 = My Enquiries, 2 = Guidelines & Policies

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AssamCreamBackground)
            .testTag("customer_hub_screen")
    ) {
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = AssamForestGreen,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "মোৰ গ্ৰাহক একাউণ্ট" else "My Customer Account",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AssamForestGreen
                        )
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "সংৰক্ষিত সামগ্ৰী আৰু অনুসন্ধানসমূহ" else "Saved products & direct seller enquiries",
                            fontSize = 12.sp,
                            color = AssamTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = AssamSurfaceVariant
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("${if (language == AppLanguage.ASSAMESE) "পছন্দৰ সামগ্ৰী" else "Wishlist"} (${wishlistedProducts.size})", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("${if (language == AppLanguage.ASSAMESE) "মোৰ অনুসন্ধান" else "Enquiries"} (${buyerEnquiries.size})", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text(if (language == AppLanguage.ASSAMESE) "নীতি আৰু নিয়ম" else "Policies", fontWeight = FontWeight.Bold) }
                    )
                }
            }
        }

        // TAB 0: Wishlist
        if (selectedTab == 0) {
            if (wishlistedProducts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "কোনো সামগ্ৰী সংৰক্ষণ কৰা হোৱা নাই" else "No saved products yet",
                            fontWeight = FontWeight.Bold,
                            color = AssamTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "পছন্দৰ সামগ্ৰীত থকা হৃদয় চিহ্নত টেপ কৰি সংৰক্ষণ কৰক।" else "Tap the heart icon on any product to save it here.",
                            fontSize = 12.sp,
                            color = AssamTextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentPadding = PaddingValues(bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val pairs = wishlistedProducts.chunked(2)
                    items(pairs) { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            pair.forEach { prod ->
                                Box(modifier = Modifier.weight(1f)) {
                                    ProductCard(
                                        product = prod,
                                        language = language,
                                        isWishlisted = true,
                                        onProductClick = { onProductClick(prod.id) },
                                        onContactSeller = { onContactSeller(prod) },
                                        onToggleWishlist = { onToggleWishlist(prod.id) }
                                    )
                                }
                            }
                            if (pair.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        // TAB 1: Enquiries
        if (selectedTab == 1) {
            if (buyerEnquiries.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "আপুনি এতিয়ালৈ কোনো অনুসন্ধান পঠোৱা নাই।" else "No enquiries submitted yet.",
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentPadding = PaddingValues(bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(buyerEnquiries) { enq ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = enq.productName,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AssamForestGreen
                                    )
                                    Surface(
                                        color = Color(0xFFE8F5E9),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = enq.status,
                                            color = AssamForestGreen,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "${if (language == AppLanguage.ASSAMESE) "পৰিমাণ: " else "Quantity: "}${enq.requestedQuantity.toInt()} ${enq.unit}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                if (enq.message.isNotBlank()) {
                                    Text(
                                        text = "“${enq.message}”",
                                        fontSize = 12.sp,
                                        color = AssamTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // TAB 2: Policies and Guidelines
        if (selectedTab == 2) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentPadding = PaddingValues(bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (language == AppLanguage.ASSAMESE) "ক্ৰেতা আৰু বিক্ৰেতাৰ নীতি-নিৰ্দেশনা" else "Marketplace Policies & Guidelines",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = AssamForestGreen
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "1. ${if (language == AppLanguage.ASSAMESE) "পোনপটীয়া যোগাযোগ: ক্ৰেতা আৰু বিক্ৰেতাই পোনপটীয়াকৈ আলোচনা কৰি সামগ্ৰীৰ দাম আৰু সংগ্ৰহৰ বন্দোবস্ত কৰে।" else "Direct Relationship: Buyers and sellers connect directly to agree on price, quantity, and fulfillment without platform commission."}\n\n" +
                                       "2. ${if (language == AppLanguage.ASSAMESE) "সঠিক তথ্য: সকলো কৃষক আৰু বিক্ৰেতাই সঠিক মূল্য আৰু মজুত পৰিমাণ প্ৰকাশ কৰিবলৈ বাধ্য।" else "Accurate Listings: Sellers must provide verified prices and actual harvest stock."}\n\n" +
                                       "3. ${if (language == AppLanguage.ASSAMESE) "স্বতন্ত্ৰ উদ্যোগ: আমাৰ হাট অসম এক স্বতন্ত্ৰ প্ৰচেষ্টা আৰু কোনো চৰকাৰী পৰ্টেল নহয়।" else "Independent Entity: Amar Haat Assam is an independent community agritech marketplace and is not an official Assam Government portal."}\n\n" +
                                       "4. ${if (language == AppLanguage.ASSAMESE) "নিকা আৰু সুৰক্ষিত ব্যৱসায়: কোনো আগতীয়া ধন বা অনলাইন লেনদেনৰ চাপ নাই।" else "Fair Trade: Low-cost local marketplace supporting farmers across Biswanath and all Assam districts."}",
                                fontSize = 12.sp,
                                color = AssamTextSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                item {
                    Button(
                        onClick = onSwitchToSellerMode,
                        colors = ButtonDefaults.buttonColors(containerColor = AssamTeaLeafGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Icon(Icons.Default.Agriculture, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (language == AppLanguage.ASSAMESE) "কৃষক / বিক্ৰেতা মোডলৈ যাওক" else "Switch to Farmer / Seller Mode")
                    }
                }
            }
        }
    }
}
