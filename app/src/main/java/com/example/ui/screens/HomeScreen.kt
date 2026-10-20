package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.CategoryEntity
import com.example.data.local.DistrictEntity
import com.example.data.local.ProductEntity
import com.example.data.local.SellerEntity
import com.example.ui.components.AvailabilityBadge
import com.example.ui.components.PilotZoneBadge
import com.example.ui.components.ProducerCard
import com.example.ui.components.ProductCard
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.AppStrings
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    products: List<ProductEntity>,
    categories: List<CategoryEntity>,
    districts: List<DistrictEntity>,
    sellers: List<SellerEntity>,
    wishlistIds: List<String>,
    language: AppLanguage,
    onSearchSubmit: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onDistrictSelected: (String) -> Unit,
    onProductClick: (String) -> Unit,
    onContactSeller: (ProductEntity) -> Unit,
    onToggleWishlist: (String) -> Unit,
    onNavigateToExplore: () -> Unit,
    onNavigateToFarmers: () -> Unit,
    onNavigateToBulk: () -> Unit,
    onRegisterSellerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchInput by remember { mutableStateOf("") }
    var selectedDistrictId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AssamCreamBackground)
            .testTag("home_screen_scroll"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. Hero Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                // Background Assamese Landscape Hero Image
                Image(
                    painter = painterResource(id = R.drawable.assam_farm_hero),
                    contentDescription = "Assam Tea & Farm Landscape",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark gradient overlay for readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x6611352A),
                                    Color(0x9911352A),
                                    Color(0xDD11352A)
                                )
                            )
                        )
                )

                // Hero Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    PilotZoneBadge(
                        text = if (language == AppLanguage.ASSAMESE) "পাইলট অঞ্চল: চতিয়া • আমবাৰী • বিশ্বনাথ" else "Pilot: Sootea • Ambari • Biswanath"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (language == AppLanguage.ASSAMESE)
                            "অসমৰ শ্ৰেষ্ঠ সামগ্ৰী, পোনপটীয়াকৈ স্থানীয় উৎপাদকৰ পৰা"
                        else
                            "Discover the Best of Assam, Direct from Local Producers",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 25.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (language == AppLanguage.ASSAMESE)
                            "সতেজ কৃষি সামগ্ৰী, পৰম্পৰাগত খাদ্য আৰু থলুৱা হস্তশিল্পৰ বিশ্বাসযোগ্য বজাৰ।"
                        else
                            "Fresh agricultural produce, traditional food, and handmade crafts across Assam.",
                        color = Color(0xFFE2EFE5),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Search input
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(horizontal = 10.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = AssamForestGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        TextField(
                            value = searchInput,
                            onValueChange = { searchInput = it },
                            placeholder = {
                                Text(
                                    text = if (language == AppLanguage.ASSAMESE) "চাউল, চাহ, কাজী নেমু, জাপি..." else "Rice, tea, kaji nemu, crafts...",
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("home_search_input")
                        )
                        IconButton(
                            onClick = {
                                if (searchInput.isNotBlank()) {
                                    onSearchSubmit(searchInput)
                                    onNavigateToExplore()
                                }
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .background(AssamForestGreen, CircleShape)
                                .testTag("home_search_submit_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Go",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // 2. District Quick Filter Chips
        item {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "জিলা অনুসৰি বিচাৰক" else "Explore by District",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AssamTextPrimary
                    )
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "সকলো জিলা" else "All Districts",
                        fontSize = 12.sp,
                        color = AssamForestGreen,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            selectedDistrictId = null
                            onDistrictSelected("all")
                            onNavigateToExplore()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedDistrictId == null,
                            onClick = {
                                selectedDistrictId = null
                                onDistrictSelected("all")
                            },
                            label = { Text(if (language == AppLanguage.ASSAMESE) "সকলো জিলা" else "All Assam") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AssamForestGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }

                    items(districts) { district ->
                        val isSelected = selectedDistrictId == district.id
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedDistrictId = district.id
                                onDistrictSelected(district.id)
                                onNavigateToExplore()
                            },
                            label = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (district.isPilotZone) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = if (isSelected) Color.White else AssamGoldenPaddy,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                    }
                                    Text(if (language == AppLanguage.ASSAMESE) district.nameAs else district.nameEn)
                                }
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AssamForestGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // 3. Product Categories Grid
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "সামগ্ৰীৰ শ্ৰেণীসমূহ" else "Product Categories",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = AssamTextPrimary
                    )
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "সকলো চাওক" else "View All",
                        fontSize = 13.sp,
                        color = AssamForestGreen,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onNavigateToExplore() }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Render categories in 3 columns
                val chunked = categories.chunked(3)
                chunked.forEach { rowCategories ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowCategories.forEach { category ->
                            val catName = if (language == AppLanguage.ASSAMESE) category.nameAs else category.nameEn
                            val icon: ImageVector = when (category.id) {
                                "cat_rice" -> Icons.Default.Grass
                                "cat_tea" -> Icons.Default.LocalCafe
                                "cat_veg" -> Icons.Default.Eco
                                "cat_fruits" -> Icons.Default.Spa
                                "cat_spices" -> Icons.Default.Whatshot
                                "cat_bamboo" -> Icons.Default.Yard
                                "cat_pickles" -> Icons.Default.Liquor
                                "cat_traditional" -> Icons.Default.Restaurant
                                "cat_handloom" -> Icons.Default.Checkroom
                                "cat_organic" -> Icons.Default.Spa
                                else -> Icons.Default.Inventory2
                            }

                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(96.dp)
                                    .clickable {
                                        onCategorySelected(category.id)
                                        onNavigateToExplore()
                                    }
                                    .testTag("category_card_${category.id}")
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .background(AssamTeaLight, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = catName,
                                            tint = AssamForestGreen,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = catName,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = AssamTextPrimary,
                                        textAlign = TextAlign.Center,
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // 4. Featured Local Products Section
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "বাছকবনীয়া থলুৱা সামগ্ৰী" else "Featured Local Products",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = AssamTextPrimary
                        )
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "পোনপটীয়াকৈ চতিয়া, আমবাৰী আৰু বিশ্বনাথৰ পৰা" else "Direct from Sootea, Ambari & Biswanath",
                            fontSize = 11.sp,
                            color = AssamTextSecondary
                        )
                    }

                    TextButton(onClick = onNavigateToExplore) {
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "অধিক চাওক" else "See More",
                            color = AssamForestGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Display first 4 featured products in 2-column or list
                val featuredList = products.take(6)
                val pairs = featuredList.chunked(2)
                pairs.forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        pair.forEach { prod ->
                            Box(modifier = Modifier.weight(1f)) {
                                ProductCard(
                                    product = prod,
                                    language = language,
                                    isWishlisted = wishlistIds.contains(prod.id),
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
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        // 5. Discover Farmers & SHG Producers
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "কৃষক আৰু উৎপাদকসকল" else "Discover Farmers & Producers",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = AssamTextPrimary
                        )
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "প্ৰমাণিত আত্মসহায়ক গোট আৰু স্থানীয় কাৰিকৰ" else "Verified SHGs, farmers, & local artisans",
                            fontSize = 11.sp,
                            color = AssamTextSecondary
                        )
                    }

                    TextButton(onClick = onNavigateToFarmers) {
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "সকলো" else "View All",
                            color = AssamForestGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                sellers.take(3).forEach { seller ->
                    ProducerCard(
                        seller = seller,
                        language = language,
                        onViewProducts = {
                            onSearchSubmit(seller.farmOrBusinessName)
                            onNavigateToExplore()
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        // 6. How It Works Section
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "আমাৰ হাটে কেনেদৰে কাম কৰে" else "How Amar Haat Works",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AssamForestGreen
                    )
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "সহজ, নিকা আৰু স্বচ্ছ প্ৰক্ৰিয়া" else "Simple, transparent, low-cost marketplace",
                        fontSize = 12.sp,
                        color = AssamTextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Buyer Steps
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "🛍️ ক্ৰেতাসকলৰ বাবে:" else "🛍️ For Buyers:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AssamTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "1. ${if (language == AppLanguage.ASSAMESE) "জিলা বাচি সামগ্ৰী আৱিষ্কাৰ কৰক" else "Browse local products in your district"}\n" +
                               "2. ${if (language == AppLanguage.ASSAMESE) "মূল্য আৰু পৰিমাণ পৰীক্ষা কৰক" else "Check transparent seller prices & stock"}\n" +
                               "3. ${if (language == AppLanguage.ASSAMESE) "হোৱাটছএপ বা ফোনেৰে পোনপটীয়াকৈ যোগাযোগ কৰক" else "Contact producer directly via call or WhatsApp"}\n" +
                               "4. ${if (language == AppLanguage.ASSAMESE) "পোনপটীয়াকৈ সংগ্ৰহ বা ডেলিভাৰীৰ বন্দোবস্ত কৰক" else "Arrange direct pickup or local delivery"}",
                        fontSize = 12.sp,
                        color = AssamTextSecondary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Seller Steps
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "🚜 কৃষক আৰু উৎপাদকসকলৰ বাবে:" else "🚜 For Farmers & Sellers:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AssamTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "1. ${if (language == AppLanguage.ASSAMESE) "বিনামূলীয়াকৈ প্ৰফাইল পঞ্জীয়ন কৰক" else "Free basic producer registration"}\n" +
                               "2. ${if (language == AppLanguage.ASSAMESE) "সামগ্ৰীৰ মূল্য আৰু চপোৱা তাৰিখ উল্লেখ কৰক" else "List products with price and harvest status"}\n" +
                               "3. ${if (language == AppLanguage.ASSAMESE) "গুণমানৰ বাবে প্ৰশাসকে অনুমোদন জনায়" else "Admin approval maintains genuine listings"}\n" +
                               "4. ${if (language == AppLanguage.ASSAMESE) "ক্ৰেতাৰ পৰা পোনপটীয়া অৰ্ডাৰ আৰু অনুসন্ধান পাওক" else "Receive genuine buyer inquiries without commission"}",
                        fontSize = 12.sp,
                        color = AssamTextSecondary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onRegisterSellerClick,
                        colors = ButtonDefaults.buttonColors(containerColor = AssamTeaLeafGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddBusiness,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "আমাৰ হাটত বিক্ৰী কৰক (বিনামূলীয়া)" else "Sell on Amar Haat (Free Basic)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 7. Bulk Buyer Requirement Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8F3)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC8E6C9)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = AssamForestGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "পাইকাৰী ক্ৰেতাৰ সুবিধা" else "Bulk Buyer Connect",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AssamForestGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (language == AppLanguage.ASSAMESE)
                            "ৰেষ্টুৰেণ্ট, হোটেল, গেলামাল দোকান আৰু পাইকাৰী ব্যৱসায়ীসকলৰ বাবে সতেজ কৃষি সামগ্ৰীৰ প্ৰয়োজনীয়তা পোনপটীয়াকৈ কৃষকসকলক জনাওক।"
                        else
                            "For restaurants, hotels, grocery stores & retailers looking for bulk farm supply. Post your requirements directly to local producers.",
                        fontSize = 12.sp,
                        color = AssamTextSecondary,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onNavigateToBulk,
                        colors = ButtonDefaults.buttonColors(containerColor = AssamForestGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "পাইকাৰী প্ৰয়োজনীয়তা জমা দিয়ক" else "Post Bulk Requirement",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 8. Local Impact & Legal Disclaimer Footer
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (language == AppLanguage.ASSAMESE) "আমাৰ হাট অসম" else "Amar Haat Assam",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AssamForestGreen
                )
                Text(
                    text = if (language == AppLanguage.ASSAMESE) "অসমৰ পথাৰৰ পৰা প্ৰতিটো ঘৰলৈ" else "From Assam's Farms to Every Home",
                    fontSize = 12.sp,
                    color = AssamGoldenPaddy,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (language == AppLanguage.ASSAMESE)
                        "স্বচ্ছ মূল্য • কৃষক সৱলীকৰণ • থলুৱা অসমীয়া সংস্কৃতি\n\nটোকা: আমাৰ হাট অসম এখন স্বতন্ত্ৰ সম্প্ৰদায় বজাৰ আৰু অসম চৰকাৰৰ চৰকাৰী পৰ্টেল নহয়।"
                    else
                        "Transparent Pricing • Rural Empowerment • Assamese Heritage\n\nNotice: Amar Haat Assam is an independent community marketplace and is not an official Assam Government portal.",
                    fontSize = 11.sp,
                    color = AssamTextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "© 2026 Amar Haat Assam. Designed for rural empowerment.",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
