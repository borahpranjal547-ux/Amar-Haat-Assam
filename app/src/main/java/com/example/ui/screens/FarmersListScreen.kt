package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DistrictEntity
import com.example.data.local.SellerEntity
import com.example.ui.components.ProducerCard
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.AssamCreamBackground
import com.example.ui.theme.AssamForestGreen
import com.example.ui.theme.AssamGoldenPaddy
import com.example.ui.theme.AssamTextPrimary

@Composable
fun FarmersListScreen(
    sellers: List<SellerEntity>,
    districts: List<DistrictEntity>,
    language: AppLanguage,
    onViewSellerProducts: (SellerEntity) -> Unit,
    onRegisterSellerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDistrictId by remember { mutableStateOf<String?>(null) }

    val filteredSellers = remember(sellers, selectedDistrictId) {
        if (selectedDistrictId == null) sellers
        else {
            val dist = districts.find { it.id == selectedDistrictId }
            if (dist != null) sellers.filter { it.district.equals(dist.nameEn, ignoreCase = true) }
            else sellers
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AssamCreamBackground)
            .testTag("farmers_screen")
    ) {
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "অসমৰ কৃষক আৰু উৎপাদকসকল" else "Assam Farmers & Producers",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AssamForestGreen
                        )
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "প্ৰমাণিত কৃষক, আত্মসহায়ক গোট আৰু শিল্পীসকল" else "Verified farmers, SHG collectives & rural artisans",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    Button(
                        onClick = onRegisterSellerClick,
                        colors = ButtonDefaults.buttonColors(containerColor = AssamForestGreen),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddBusiness,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "পঞ্জীয়ন" else "Join as Seller",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // District filter chips
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    item {
                        FilterChip(
                            selected = selectedDistrictId == null,
                            onClick = { selectedDistrictId = null },
                            label = { Text(if (language == AppLanguage.ASSAMESE) "সকলো জিলা" else "All Districts") },
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
                            onClick = { selectedDistrictId = if (isSelected) null else district.id },
                            label = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (district.isPilotZone) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = if (isSelected) Color.White else AssamGoldenPaddy,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = PaddingValues(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredSellers) { seller ->
                ProducerCard(
                    seller = seller,
                    language = language,
                    onViewProducts = { onViewSellerProducts(seller) }
                )
            }
        }
    }
}
