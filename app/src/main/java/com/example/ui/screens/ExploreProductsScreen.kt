package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.data.local.CategoryEntity
import com.example.data.local.DistrictEntity
import com.example.data.local.ProductEntity
import com.example.ui.SortOrder
import com.example.ui.components.ProductCard
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreProductsScreen(
    products: List<ProductEntity>,
    categories: List<CategoryEntity>,
    districts: List<DistrictEntity>,
    wishlistIds: List<String>,
    searchQuery: String,
    selectedDistrictId: String?,
    selectedCategoryId: String?,
    selectedAvailability: String?,
    sortOrder: SortOrder,
    language: AppLanguage,
    onSearchChange: (String) -> Unit,
    onDistrictChange: (String?) -> Unit,
    onCategoryChange: (String?) -> Unit,
    onAvailabilityChange: (String?) -> Unit,
    onSortChange: (SortOrder) -> Unit,
    onProductClick: (String) -> Unit,
    onContactSeller: (ProductEntity) -> Unit,
    onToggleWishlist: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterSheet by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AssamCreamBackground)
            .testTag("explore_screen")
    ) {
        // Search & Filter header
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Search TextField
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = AssamForestGreen
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = Color.Gray
                                )
                            }
                        }
                    },
                    placeholder = {
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "চাউল, কাজী নেমু, শাক, জাপি..." else "Search rice, tea, vegetables, crafts...",
                            fontSize = 13.sp
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("explore_search_bar")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Filter Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Quick district selector chip
                    AssistChip(
                        onClick = { showFilterSheet = true },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Filter",
                                tint = AssamForestGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        label = {
                            val count = (if (selectedDistrictId != null) 1 else 0) +
                                    (if (selectedCategoryId != null) 1 else 0) +
                                    (if (selectedAvailability != null) 1 else 0)
                            Text(
                                text = if (count > 0)
                                    "${if (language == AppLanguage.ASSAMESE) "ফিল্টাৰ" else "Filters"} ($count)"
                                else
                                    if (language == AppLanguage.ASSAMESE) "ফিল্টাৰ" else "Filters",
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (selectedDistrictId != null || selectedCategoryId != null || selectedAvailability != null)
                                AssamTeaLight else Color(0xFFF3F4F6)
                        )
                    )

                    // Sort order chip
                    AssistChip(
                        onClick = {
                            val nextSort = when (sortOrder) {
                                SortOrder.NEWEST -> SortOrder.PRICE_LOW_HIGH
                                SortOrder.PRICE_LOW_HIGH -> SortOrder.PRICE_HIGH_LOW
                                SortOrder.PRICE_HIGH_LOW -> SortOrder.NEWEST
                            }
                            onSortChange(nextSort)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = "Sort",
                                tint = AssamGoldenPaddy,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        label = {
                            val label = when (sortOrder) {
                                SortOrder.NEWEST -> if (language == AppLanguage.ASSAMESE) "নতুনকৈ যোগ কৰা" else "Newest"
                                SortOrder.PRICE_LOW_HIGH -> if (language == AppLanguage.ASSAMESE) "মূল্য: কমৰ পৰা বেছি" else "Price: Low to High"
                                SortOrder.PRICE_HIGH_LOW -> if (language == AppLanguage.ASSAMESE) "মূল্য: বেছিৰ পৰা কম" else "Price: High to Low"
                            }
                            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    )

                    // Reset button if filters active
                    if (selectedDistrictId != null || selectedCategoryId != null || selectedAvailability != null || searchQuery.isNotBlank()) {
                        TextButton(
                            onClick = {
                                onSearchChange("")
                                onDistrictChange(null)
                                onCategoryChange(null)
                                onAvailabilityChange(null)
                            }
                        ) {
                            Text(
                                text = if (language == AppLanguage.ASSAMESE) "ৰিষ্টেট" else "Reset",
                                color = Color.Red,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Active filter pills
        LazyRow(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedCategoryId == null,
                    onClick = { onCategoryChange(null) },
                    label = { Text(if (language == AppLanguage.ASSAMESE) "সকলো শ্ৰেণী" else "All Categories") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AssamForestGreen, selectedLabelColor = Color.White)
                )
            }
            items(categories) { cat ->
                val isSel = selectedCategoryId == cat.id
                FilterChip(
                    selected = isSel,
                    onClick = { onCategoryChange(if (isSel) null else cat.id) },
                    label = { Text(if (language == AppLanguage.ASSAMESE) cat.nameAs else cat.nameEn) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AssamForestGreen, selectedLabelColor = Color.White)
                )
            }
        }

        // Results counter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${products.size} ${if (language == AppLanguage.ASSAMESE) "টা সামগ্ৰী পোৱা গ’ল" else "products found"}",
                fontSize = 12.sp,
                color = AssamTextSecondary,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Products List or Empty State
        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = "No Products",
                        tint = Color.Gray,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "কোনো সামগ্ৰী পোৱা নগ’ল" else "No products found",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AssamTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (language == AppLanguage.ASSAMESE)
                            "অনুগ্ৰহ কৰি ফিল্টাৰ বা সন্ধান সলনি কৰি পুনৰ চেষ্টা কৰক।"
                        else
                            "Try searching with different keywords or reset your active filters.",
                        fontSize = 12.sp,
                        color = AssamTextSecondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            onSearchChange("")
                            onDistrictChange(null)
                            onCategoryChange(null)
                            onAvailabilityChange(null)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AssamForestGreen)
                    ) {
                        Text(if (language == AppLanguage.ASSAMESE) "সকলো ফিল্টাৰ আঁতৰাওক" else "Clear All Filters")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                contentPadding = PaddingValues(top = 4.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val pairs = products.chunked(2)
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
                }
            }
        }
    }

    // Filter Bottom Sheet
    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.ASSAMESE) "ফিল্টাৰসমূহ" else "Filter Products",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AssamForestGreen
                )

                Spacer(modifier = Modifier.height(14.dp))

                // District selector
                Text(
                    text = if (language == AppLanguage.ASSAMESE) "জিলা বাছক:" else "Select District:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    item {
                        FilterChip(
                            selected = selectedDistrictId == null,
                            onClick = { onDistrictChange(null) },
                            label = { Text(if (language == AppLanguage.ASSAMESE) "সকলো জিলা" else "All Districts") }
                        )
                    }
                    items(districts) { dist ->
                        FilterChip(
                            selected = selectedDistrictId == dist.id,
                            onClick = { onDistrictChange(if (selectedDistrictId == dist.id) null else dist.id) },
                            label = { Text(if (language == AppLanguage.ASSAMESE) dist.nameAs else dist.nameEn) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Availability status selector
                Text(
                    text = if (language == AppLanguage.ASSAMESE) "মজুত স্থিতি:" else "Stock Availability:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = selectedAvailability == null,
                        onClick = { onAvailabilityChange(null) },
                        label = { Text(if (language == AppLanguage.ASSAMESE) "সকলো" else "All") }
                    )
                    FilterChip(
                        selected = selectedAvailability == "AVAILABLE_NOW",
                        onClick = { onAvailabilityChange(if (selectedAvailability == "AVAILABLE_NOW") null else "AVAILABLE_NOW") },
                        label = { Text(if (language == AppLanguage.ASSAMESE) "এতিয়া মজুত" else "Available Now") }
                    )
                    FilterChip(
                        selected = selectedAvailability == "FUTURE_HARVEST",
                        onClick = { onAvailabilityChange(if (selectedAvailability == "FUTURE_HARVEST") null else "FUTURE_HARVEST") },
                        label = { Text(if (language == AppLanguage.ASSAMESE) "ভৱিষ্যত চপোৱা" else "Future Harvest") }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { showFilterSheet = false },
                    colors = ButtonDefaults.buttonColors(containerColor = AssamForestGreen),
                    modifier = Modifier.fillMaxWidth().height(46.dp)
                ) {
                    Text(if (language == AppLanguage.ASSAMESE) "প্ৰয়োগ কৰক" else "Apply Filters")
                }
            }
        }
    }
}
