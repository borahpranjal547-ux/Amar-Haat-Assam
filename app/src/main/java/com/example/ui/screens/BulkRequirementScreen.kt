package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BulkRequirementEntity
import com.example.data.local.DistrictEntity
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.*

@Composable
fun BulkRequirementScreen(
    requirements: List<BulkRequirementEntity>,
    districts: List<DistrictEntity>,
    language: AppLanguage,
    onSubmitBulkRequirement: (
        buyerName: String,
        businessName: String,
        mobileNumber: String,
        email: String,
        district: String,
        productRequired: String,
        requiredQty: Double,
        unit: String,
        expectedPrice: String,
        deliveryDate: String,
        additionalRequirements: String
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Post Form, 1 = Recent Requests

    // Form fields
    var buyerName by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedDistrict by remember { mutableStateOf("Biswanath") }
    var productRequired by remember { mutableStateOf("") }
    var quantityText by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("kg") }
    var expectedPrice by remember { mutableStateOf("") }
    var deliveryDate by remember { mutableStateOf("") }
    var additionalNotes by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AssamCreamBackground)
            .testTag("bulk_requirement_screen")
    ) {
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
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
                        text = if (language == AppLanguage.ASSAMESE) "পাইকাৰী ক্ৰেতা সেৱা" else "Bulk Order Connect",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AssamForestGreen
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (language == AppLanguage.ASSAMESE)
                        "দোকানী, হোটেল, ৰেষ্টুৰেণ্ট আৰু পাইকাৰী ব্যৱসায়ীৰ বাবে পোনপটীয়া সংযোগ"
                    else
                        "For retail shops, hotels, restaurants, caterers & wholesalers",
                    fontSize = 12.sp,
                    color = AssamTextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = AssamSurfaceVariant
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                text = if (language == AppLanguage.ASSAMESE) "প্ৰয়োজনীয়তা জমা দিয়ক" else "Post Requirement",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                text = "${if (language == AppLanguage.ASSAMESE) "খোলা অনুৰোধসমূহ" else "Open Requests"} (${requirements.size})",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                }
            }
        }

        if (selectedTab == 0) {
            // Post Form
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
                            text = if (language == AppLanguage.ASSAMESE) "ব্যৱসায়িক তথ্য" else "Buyer / Business Details",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AssamForestGreen
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = buyerName,
                            onValueChange = {
                                buyerName = it
                                errorMessage = null
                            },
                            label = { Text(if (language == AppLanguage.ASSAMESE) "আপোনাৰ নাম *" else "Contact Person Name *") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("bulk_name_input")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = businessName,
                            onValueChange = { businessName = it },
                            label = { Text(if (language == AppLanguage.ASSAMESE) "দোকান / ব্যৱসায়ৰ নাম (ঐচ্ছিক)" else "Business / Hotel / Store Name (Optional)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("bulk_business_input")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = mobileNumber,
                            onValueChange = {
                                mobileNumber = it
                                errorMessage = null
                            },
                            label = { Text(if (language == AppLanguage.ASSAMESE) "মোবাইল নম্বৰ (১০ টা অংক) *" else "Phone Number (10 digits) *") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("bulk_phone_input")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text(if (language == AppLanguage.ASSAMESE) "ইমেইল (ঐচ্ছিক)" else "Email (Optional)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "প্ৰয়োজনীয় সামগ্ৰীৰ তথ্য" else "Product Requirement Details",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AssamForestGreen
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = productRequired,
                            onValueChange = {
                                productRequired = it
                                errorMessage = null
                            },
                            label = { Text(if (language == AppLanguage.ASSAMESE) "প্ৰয়োজনীয় সামগ্ৰী * (যেনে: জোহা চাউল, কাজী নেমু)" else "Product Required * (e.g., Joha Rice, Kaji Nemu)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("bulk_product_input")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = quantityText,
                                onValueChange = {
                                    quantityText = it
                                    errorMessage = null
                                },
                                label = { Text(if (language == AppLanguage.ASSAMESE) "পৰিমাণ *" else "Quantity *") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f).testTag("bulk_qty_input")
                            )

                            // Unit selector chips / text
                            var expandedUnit by remember { mutableStateOf(false) }
                            Box(modifier = Modifier.width(110.dp)) {
                                OutlinedButton(
                                    onClick = { expandedUnit = true },
                                    modifier = Modifier.fillMaxWidth().height(56.dp)
                                ) {
                                    Text(unit)
                                }
                                DropdownMenu(
                                    expanded = expandedUnit,
                                    onDismissRequest = { expandedUnit = false }
                                ) {
                                    listOf("kg", "quintal", "piece", "packet", "bundle", "litre").forEach { u ->
                                        DropdownMenuItem(
                                            text = { Text(u) },
                                            onClick = {
                                                unit = u
                                                expandedUnit = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = expectedPrice,
                            onValueChange = { expectedPrice = it },
                            label = { Text(if (language == AppLanguage.ASSAMESE) "আশাকৃত মূল্য প্ৰতি এককত (ঐচ্ছিক)" else "Expected Target Price per unit (Optional)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = deliveryDate,
                            onValueChange = { deliveryDate = it },
                            label = { Text(if (language == AppLanguage.ASSAMESE) "ডেলিভাৰীৰ সময় / তাৰিখ" else "Required By / Delivery Date") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = additionalNotes,
                            onValueChange = { additionalNotes = it },
                            label = { Text(if (language == AppLanguage.ASSAMESE) "অন্যান্য চৰ্ত বা পেকেজিং নিৰ্দেশনা" else "Packaging or special requirements") },
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (errorMessage != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = errorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val qty = quantityText.toDoubleOrNull()
                                if (buyerName.isBlank()) {
                                    errorMessage = "Please enter your contact name."
                                } else if (mobileNumber.length < 10) {
                                    errorMessage = "Please enter a valid 10-digit mobile number."
                                } else if (productRequired.isBlank()) {
                                    errorMessage = "Please specify the required product."
                                } else if (qty == null || qty <= 0) {
                                    errorMessage = "Please enter a valid quantity."
                                } else {
                                    onSubmitBulkRequirement(
                                        buyerName.trim(),
                                        businessName.trim(),
                                        mobileNumber.trim(),
                                        email.trim(),
                                        selectedDistrict,
                                        productRequired.trim(),
                                        qty,
                                        unit,
                                        expectedPrice.trim(),
                                        deliveryDate.trim(),
                                        additionalNotes.trim()
                                    )
                                    // Reset fields
                                    buyerName = ""
                                    businessName = ""
                                    mobileNumber = ""
                                    productRequired = ""
                                    quantityText = ""
                                    selectedTab = 1
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AssamForestGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("submit_bulk_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PostAdd,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.ASSAMESE) "প্ৰয়োজনীয়তা জমা দিয়ক" else "Submit Bulk Requirement",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        } else {
            // Recent Requests Tab
            if (requirements.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "কোনো পাইকাৰী অনুৰোধ নাই। প্ৰথম অনুৰোধ জমা দিয়ক!" else "No open bulk requirements yet. Post the first one!",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentPadding = PaddingValues(bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(requirements) { req ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
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
                                        text = req.productRequired,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AssamForestGreen
                                    )
                                    Surface(
                                        color = Color(0xFFE8F5E9),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = req.status,
                                            color = AssamForestGreen,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "${if (language == AppLanguage.ASSAMESE) "প্ৰয়োজন: " else "Required: "}${req.requiredQuantity.toInt()} ${req.unit}${if (req.expectedPrice.isNotBlank()) " • Target: ${req.expectedPrice}" else ""}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AssamTextPrimary
                                )

                                Text(
                                    text = "${if (language == AppLanguage.ASSAMESE) "ক্ৰেতা: " else "Buyer: "}${req.buyerName}${if (req.businessName.isNotBlank()) " (${req.businessName})" else ""} • ${req.district}",
                                    fontSize = 12.sp,
                                    color = AssamTextSecondary
                                )

                                if (req.requiredDeliveryDate.isNotBlank()) {
                                    Text(
                                        text = "${if (language == AppLanguage.ASSAMESE) "তাৰিখ: " else "Need by: "}${req.requiredDeliveryDate}",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
