package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.local.BulkRequirementEntity
import com.example.data.local.EnquiryEntity
import com.example.data.local.ProductEntity
import com.example.data.local.SellerEntity
import com.example.ui.components.AvailabilityBadge
import com.example.ui.components.VerificationBadge
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    allProducts: List<ProductEntity>,
    allSellers: List<SellerEntity>,
    allEnquiries: List<EnquiryEntity>,
    allBulkRequests: List<BulkRequirementEntity>,
    language: AppLanguage,
    onApproveProduct: (productId: String, approve: Boolean) -> Unit,
    onApproveSeller: (sellerId: String, approve: Boolean) -> Unit,
    onUpdateBulkStatus: (reqId: String, status: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Overview, 1 = Products, 2 = Sellers, 3 = Bulk Orders

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AssamCreamBackground)
            .testTag("admin_dashboard_screen")
    ) {
        Surface(
            color = Color(0xFF1E293B),
            contentColor = Color.White,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFF334155), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "আমাৰ হাট প্ৰশাসন পেনেল" else "Amar Haat Admin Panel",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "গুণমান নিৰীক্ষণ আৰু অনুমোদন ব্যৱস্থা" else "Marketplace Moderation & Verification",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFF0F172A),
                    contentColor = Color.White,
                    edgePadding = 0.dp
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Overview") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Products (${allProducts.size})") }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("Sellers (${allSellers.size})") }
                    )
                    Tab(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        text = { Text("Bulk Requests (${allBulkRequests.size})") }
                    )
                }
            }
        }

        // TAB 0: Overview Metrics
        if (selectedTab == 0) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentPadding = PaddingValues(bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Real-Time Platform Metrics (Assam)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AssamTextPrimary
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(allSellers.size.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = AssamForestGreen)
                                Text("Registered Sellers", fontSize = 11.sp, color = AssamTextSecondary)
                            }
                        }
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(allProducts.size.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = AssamTeaLeafGreen)
                                Text("Product Listings", fontSize = 11.sp, color = AssamTextSecondary)
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(allEnquiries.size.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = AssamGoldenPaddy)
                                Text("Buyer Enquiries", fontSize = 11.sp, color = AssamTextSecondary)
                            }
                        }
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(allBulkRequests.size.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                                Text("Bulk Requirements", fontSize = 11.sp, color = AssamTextSecondary)
                            }
                        }
                    }
                }

                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Pilot Deployment Region", fontWeight = FontWeight.Bold, color = AssamForestGreen)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Initial Phase: Sootea • Ambari • Biswanath Chariali\nCoverage: 35 Assam Districts pre-configured with rural producer directories.",
                                fontSize = 12.sp,
                                color = AssamTextSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }

        // TAB 1: Product Moderation
        if (selectedTab == 1) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentPadding = PaddingValues(bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(allProducts) { prod ->
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
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(prod.nameEn, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                    Text("${prod.sellerName} • ${prod.sellerVillage}, ${prod.sellerDistrict}", fontSize = 11.sp, color = AssamTextSecondary)
                                    Text("₹${prod.price.toInt()}/${prod.unit} • Qty: ${prod.availableQuantity.toInt()} ${prod.unit}", fontSize = 12.sp, color = AssamForestGreen, fontWeight = FontWeight.SemiBold)
                                }

                                Surface(
                                    color = if (prod.approvalStatus == "APPROVED") Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = prod.approvalStatus,
                                        color = if (prod.approvalStatus == "APPROVED") Color(0xFF16A34A) else Color(0xFFD97706),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (prod.approvalStatus != "APPROVED") {
                                    Button(
                                        onClick = { onApproveProduct(prod.id, true) },
                                        colors = ButtonDefaults.buttonColors(containerColor = AssamForestGreen),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Approve Listing", fontSize = 12.sp)
                                    }
                                } else {
                                    OutlinedButton(
                                        onClick = { onApproveProduct(prod.id, false) },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Reject / Hide", fontSize = 12.sp, color = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // TAB 2: Seller Verification & Moderation
        if (selectedTab == 2) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentPadding = PaddingValues(bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(allSellers) { seller ->
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
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(seller.farmOrBusinessName, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                    Text("${seller.fullName} • ${seller.sellerType}", fontSize = 12.sp, color = AssamTextSecondary)
                                    Text("📍 ${seller.village}, ${seller.district} • 📞 ${seller.mobileNumber}", fontSize = 11.sp, color = AssamForestGreen)
                                }

                                Surface(
                                    color = if (seller.approvalStatus == "APPROVED") Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = seller.approvalStatus,
                                        color = if (seller.approvalStatus == "APPROVED") Color(0xFF16A34A) else Color(0xFFD97706),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (seller.approvalStatus != "APPROVED") {
                                    Button(
                                        onClick = { onApproveSeller(seller.id, true) },
                                        colors = ButtonDefaults.buttonColors(containerColor = AssamForestGreen),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Verify & Approve", fontSize = 12.sp)
                                    }
                                } else {
                                    OutlinedButton(
                                        onClick = { onApproveSeller(seller.id, false) },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Suspend Account", fontSize = 12.sp, color = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // TAB 3: Bulk Requests Oversight
        if (selectedTab == 3) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentPadding = PaddingValues(bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(allBulkRequests) { req ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(req.productRequired, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AssamForestGreen)
                            Text("Buyer: ${req.buyerName} (${req.mobileNumber}) • ${req.district}", fontSize = 12.sp)
                            Text("Qty: ${req.requiredQuantity.toInt()} ${req.unit} • Expected Price: ${req.expectedPrice}", fontSize = 12.sp, color = AssamTextSecondary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { onUpdateBulkStatus(req.id, "IN_REVIEW") },
                                    colors = ButtonDefaults.buttonColors(containerColor = AssamTeaLeafGreen),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Mark Connecting", fontSize = 11.sp)
                                }
                                Button(
                                    onClick = { onUpdateBulkStatus(req.id, "FULFILLED") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Mark Fulfilled", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
