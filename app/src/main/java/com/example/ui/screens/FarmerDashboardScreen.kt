package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CategoryEntity
import com.example.data.local.EnquiryEntity
import com.example.data.local.ProductEntity
import com.example.data.local.SellerEntity
import com.example.ui.components.AvailabilityBadge
import com.example.ui.components.VerificationBadge
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.*

@Composable
fun FarmerDashboardScreen(
    currentSeller: SellerEntity?,
    sellerProducts: List<ProductEntity>,
    sellerEnquiries: List<EnquiryEntity>,
    categories: List<CategoryEntity>,
    language: AppLanguage,
    onAddProduct: (
        nameEn: String,
        nameAs: String,
        categoryId: String,
        categoryNameEn: String,
        categoryNameAs: String,
        descEn: String,
        descAs: String,
        price: Double,
        unit: String,
        availableQty: Double,
        minOrderQty: Double,
        availabilityStatus: String,
        harvestDate: String,
        pickupAvailable: Boolean,
        deliveryAvailable: Boolean,
        isOrganic: Boolean
    ) -> Unit,
    onToggleAvailability: (productId: String, currentStatus: String) -> Unit,
    onUpdatePriceStock: (productId: String, newPrice: Double, newStock: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) } // 0 = My Listings, 1 = Buyer Enquiries, 2 = Add Product

    // Add Product Form State
    var prodNameEn by remember { mutableStateOf("") }
    var prodNameAs by remember { mutableStateOf("") }
    var selectedCatId by remember { mutableStateOf(categories.firstOrNull()?.id ?: "cat_veg") }
    var prodPriceText by remember { mutableStateOf("") }
    var prodStockText by remember { mutableStateOf("") }
    var prodMinQtyText by remember { mutableStateOf("1") }
    var prodUnit by remember { mutableStateOf("kg") }
    var prodDescEn by remember { mutableStateOf("") }
    var prodDescAs by remember { mutableStateOf("") }
    var prodAvailability by remember { mutableStateOf("AVAILABLE_NOW") }
    var prodHarvestDate by remember { mutableStateOf("") }
    var pickupAvailable by remember { mutableStateOf(true) }
    var deliveryAvailable by remember { mutableStateOf(true) }
    var isOrganic by remember { mutableStateOf(false) }
    var formError by remember { mutableStateOf<String?>(null) }

    // Quick Edit Price Dialog
    var editingProduct by remember { mutableStateOf<ProductEntity?>(null) }
    var editPriceText by remember { mutableStateOf("") }
    var editStockText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AssamCreamBackground)
            .testTag("farmer_dashboard_screen")
    ) {
        // Header
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(AssamTeaLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Agriculture,
                            contentDescription = null,
                            tint = AssamForestGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentSeller?.farmOrBusinessName ?: "Farmer Dashboard",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AssamForestGreen
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            VerificationBadge(
                                isVerified = currentSeller?.approvalStatus == "APPROVED",
                                language = language
                            )
                        }
                        Text(
                            text = "${currentSeller?.village ?: "Sootea"}, ${currentSeller?.district ?: "Biswanath"} • Phone: ${currentSeller?.mobileNumber ?: "9435012345"}",
                            fontSize = 11.sp,
                            color = AssamTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats Overview Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = AssamSurfaceVariant),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = sellerProducts.size.toString(),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = AssamForestGreen
                            )
                            Text(
                                text = if (language == AppLanguage.ASSAMESE) "মুঠ সামগ্ৰী" else "Total Listings",
                                fontSize = 10.sp,
                                color = AssamTextSecondary
                            )
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = sellerProducts.count { it.availabilityStatus == "AVAILABLE_NOW" }.toString(),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF16A34A)
                            )
                            Text(
                                text = if (language == AppLanguage.ASSAMESE) "সক্ৰিয় সামগ্ৰী" else "Active Stock",
                                fontSize = 10.sp,
                                color = AssamTextSecondary
                            )
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = sellerEnquiries.size.toString(),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD97706)
                            )
                            Text(
                                text = if (language == AppLanguage.ASSAMESE) "ক্ৰেতাৰ অনুসন্ধান" else "Enquiries",
                                fontSize = 10.sp,
                                color = AssamTextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = AssamSurfaceVariant
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text(if (language == AppLanguage.ASSAMESE) "মোৰ সামগ্ৰী" else "My Listings", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("${if (language == AppLanguage.ASSAMESE) "অনুসন্ধান" else "Enquiries"} (${sellerEnquiries.size})", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text(if (language == AppLanguage.ASSAMESE) "+ সামগ্ৰী যোগ" else "+ Add Item", fontWeight = FontWeight.Bold) }
                    )
                }
            }
        }

        // TAB 0: My Listings
        if (selectedTab == 0) {
            if (sellerProducts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(if (language == AppLanguage.ASSAMESE) "কোনো সামগ্ৰী তালিকাভুক্ত কৰা হোৱা নাই" else "No products listed yet")
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { selectedTab = 2 },
                            colors = ButtonDefaults.buttonColors(containerColor = AssamForestGreen)
                        ) {
                            Text(if (language == AppLanguage.ASSAMESE) "নতুন সামগ্ৰী যোগ কৰক" else "Add Your First Product")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentPadding = PaddingValues(bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(sellerProducts) { prod ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth().testTag("seller_item_${prod.id}")
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (language == AppLanguage.ASSAMESE) prod.nameAs else prod.nameEn,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AssamTextPrimary
                                        )
                                        Text(
                                            text = "₹${prod.price.toInt()} / ${prod.unit} • ${prod.availableQuantity.toInt()} ${prod.unit} left",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = AssamForestGreen
                                        )
                                    }

                                    AvailabilityBadge(status = prod.availabilityStatus, language = language)
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "${if (language == AppLanguage.ASSAMESE) "মজুত নিশ্চিতকৰণ: " else "Confirmed: "}${prod.lastConfirmedDate}",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Quick toggle status button
                                    OutlinedButton(
                                        onClick = {
                                            val newStatus = if (prod.availabilityStatus == "AVAILABLE_NOW") "UNAVAILABLE" else "AVAILABLE_NOW"
                                            onToggleAvailability(prod.id, newStatus)
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = if (prod.availabilityStatus == "AVAILABLE_NOW")
                                                (if (language == AppLanguage.ASSAMESE) "বন্ধ কৰক" else "Mark Out of Stock")
                                            else
                                                (if (language == AppLanguage.ASSAMESE) "মজুত আছে" else "Mark Available"),
                                            fontSize = 11.sp
                                        )
                                    }

                                    // Quick update price & stock
                                    Button(
                                        onClick = {
                                            editingProduct = prod
                                            editPriceText = prod.price.toInt().toString()
                                            editStockText = prod.availableQuantity.toInt().toString()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = AssamForestGreen),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(if (language == AppLanguage.ASSAMESE) "মূল্য/ষ্টক সলনি" else "Edit Price/Qty", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // TAB 1: Buyer Enquiries
        if (selectedTab == 1) {
            if (sellerEnquiries.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "এতিয়ালৈ কোনো নতুন অনুসন্ধান অহা নাই।" else "No buyer enquiries received yet.",
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentPadding = PaddingValues(bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(sellerEnquiries) { enq ->
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
                                    Surface(color = Color(0xFFFEF3C7), shape = RoundedCornerShape(6.dp)) {
                                        Text(
                                            text = enq.status,
                                            color = Color(0xFFD97706),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "${if (language == AppLanguage.ASSAMESE) "বিচাৰিছে: " else "Requested: "}${enq.requestedQuantity.toInt()} ${enq.unit}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AssamTextPrimary
                                )

                                Text(
                                    text = "${if (language == AppLanguage.ASSAMESE) "ক্ৰেতা: " else "Buyer: "}${enq.buyerName} • 📞 ${enq.buyerPhone}",
                                    fontSize = 12.sp,
                                    color = AssamTextSecondary
                                )

                                if (enq.message.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "“${enq.message}”",
                                        fontSize = 12.sp,
                                        color = AssamTextPrimary
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                                data = Uri.parse("tel:${enq.buyerPhone}")
                                            }
                                            context.startActivity(intent)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = AssamForestGreen),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(if (language == AppLanguage.ASSAMESE) "ফোন কৰক" else "Call Buyer", fontSize = 12.sp)
                                    }

                                    Button(
                                        onClick = {
                                            val msg = "Hello ${enq.buyerName}, this is regarding your enquiry for ${enq.productName} on Amar Haat Assam."
                                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                                data = Uri.parse("https://wa.me/91${enq.buyerPhone}?text=${Uri.encode(msg)}")
                                            }
                                            context.startActivity(intent)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("WhatsApp", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // TAB 2: Add Product Form
        if (selectedTab == 2) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "নতুন সামগ্ৰী তালিকাভুক্ত কৰক" else "List a New Farm Product",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AssamForestGreen
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = prodNameEn,
                            onValueChange = { prodNameEn = it },
                            label = { Text("Product Name (English) *") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("add_prod_name_en")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = prodNameAs,
                            onValueChange = { prodNameAs = it },
                            label = { Text("সামগ্ৰীৰ নাম (অসমীয়াত) *") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("add_prod_name_as")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Category Dropdown
                        var expandedCat by remember { mutableStateOf(false) }
                        val currentCat = categories.find { it.id == selectedCatId }
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = { expandedCat = true },
                                modifier = Modifier.fillMaxWidth().height(56.dp)
                            ) {
                                Text(
                                    text = "${if (language == AppLanguage.ASSAMESE) "শ্ৰেণী: " else "Category: "}${if (language == AppLanguage.ASSAMESE) currentCat?.nameAs ?: "" else currentCat?.nameEn ?: ""}"
                                )
                            }
                            DropdownMenu(
                                expanded = expandedCat,
                                onDismissRequest = { expandedCat = false }
                            ) {
                                categories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(if (language == AppLanguage.ASSAMESE) cat.nameAs else cat.nameEn) },
                                        onClick = {
                                            selectedCatId = cat.id
                                            expandedCat = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = prodPriceText,
                                onValueChange = { prodPriceText = it },
                                label = { Text("Price (₹) *") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f).testTag("add_prod_price")
                            )

                            // Unit
                            var expandedUnit by remember { mutableStateOf(false) }
                            Box(modifier = Modifier.width(110.dp)) {
                                OutlinedButton(
                                    onClick = { expandedUnit = true },
                                    modifier = Modifier.fillMaxWidth().height(56.dp)
                                ) {
                                    Text(prodUnit)
                                }
                                DropdownMenu(
                                    expanded = expandedUnit,
                                    onDismissRequest = { expandedUnit = false }
                                ) {
                                    listOf("kg", "quintal", "piece", "packet", "bundle", "litre").forEach { u ->
                                        DropdownMenuItem(
                                            text = { Text(u) },
                                            onClick = {
                                                prodUnit = u
                                                expandedUnit = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = prodStockText,
                                onValueChange = { prodStockText = it },
                                label = { Text("Available Qty *") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f).testTag("add_prod_stock")
                            )

                            OutlinedTextField(
                                value = prodMinQtyText,
                                onValueChange = { prodMinQtyText = it },
                                label = { Text("Min Order") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = prodDescEn,
                            onValueChange = { prodDescEn = it },
                            label = { Text("Description (English)") },
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = prodDescAs,
                            onValueChange = { prodDescAs = it },
                            label = { Text("বিৱৰণ (অসমীয়াত)") },
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Pickup and Delivery toggles
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = pickupAvailable,
                                onCheckedChange = { pickupAvailable = it }
                            )
                            Text(if (language == AppLanguage.ASSAMESE) "পথাৰৰ পৰা সংগ্ৰহ (Pickup) সুবিধা আছে" else "Farm Pickup Available")
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = deliveryAvailable,
                                onCheckedChange = { deliveryAvailable = it }
                            )
                            Text(if (language == AppLanguage.ASSAMESE) "স্থানীয় ডেলিভাৰী দিব পৰা যাব" else "Local Delivery Available")
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = isOrganic,
                                onCheckedChange = { isOrganic = it }
                            )
                            Text(if (language == AppLanguage.ASSAMESE) "প্ৰাকৃতিক / জৈৱিক প্ৰমাণিত (Organic)" else "Organic / Chemical-free")
                        }

                        if (formError != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(formError!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val price = prodPriceText.toDoubleOrNull()
                                val stock = prodStockText.toDoubleOrNull()
                                val minQty = prodMinQtyText.toDoubleOrNull() ?: 1.0

                                if (prodNameEn.isBlank() && prodNameAs.isBlank()) {
                                    formError = "Please enter product name."
                                } else if (price == null || price <= 0) {
                                    formError = "Please enter a valid price."
                                } else if (stock == null || stock <= 0) {
                                    formError = "Please enter available stock quantity."
                                } else {
                                    val cat = categories.find { it.id == selectedCatId }
                                    onAddProduct(
                                        prodNameEn.ifBlank { prodNameAs },
                                        prodNameAs.ifBlank { prodNameEn },
                                        selectedCatId,
                                        cat?.nameEn ?: "Local Products",
                                        cat?.nameAs ?: "স্থানীয় সামগ্ৰী",
                                        prodDescEn.ifBlank { "Fresh authentic local produce." },
                                        prodDescAs.ifBlank { "সতেজ থলুৱা সামগ্ৰী।" },
                                        price,
                                        prodUnit,
                                        stock,
                                        minQty,
                                        prodAvailability,
                                        prodHarvestDate.ifBlank { "Current season" },
                                        pickupAvailable,
                                        deliveryAvailable,
                                        isOrganic
                                    )
                                    // Reset and switch to Listings tab
                                    prodNameEn = ""
                                    prodNameAs = ""
                                    prodPriceText = ""
                                    prodStockText = ""
                                    selectedTab = 0
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AssamForestGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("submit_product_button")
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (language == AppLanguage.ASSAMESE) "সামগ্ৰী প্ৰকাশ কৰক" else "Publish Listing", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Edit Price/Stock Dialog
    if (editingProduct != null) {
        AlertDialog(
            onDismissRequest = { editingProduct = null },
            title = { Text(if (language == AppLanguage.ASSAMESE) "মূল্য আৰু মজুত ষ্টক সলনি কৰক" else "Update Price & Stock") },
            text = {
                Column {
                    Text(editingProduct!!.nameEn, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = editPriceText,
                        onValueChange = { editPriceText = it },
                        label = { Text("Price (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editStockText,
                        onValueChange = { editStockText = it },
                        label = { Text("Available Quantity (${editingProduct!!.unit})") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val p = editPriceText.toDoubleOrNull()
                        val s = editStockText.toDoubleOrNull()
                        if (p != null && s != null) {
                            onUpdatePriceStock(editingProduct!!.id, p, s)
                        }
                        editingProduct = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AssamForestGreen)
                ) {
                    Text(if (language == AppLanguage.ASSAMESE) "আপডেট কৰক" else "Update")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingProduct = null }) {
                    Text(if (language == AppLanguage.ASSAMESE) "বাতিল" else "Cancel")
                }
            }
        )
    }
}
