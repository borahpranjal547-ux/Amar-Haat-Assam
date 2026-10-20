package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserRole
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.AppStrings
import com.example.ui.theme.AssamForestGreen
import com.example.ui.theme.AssamGoldenPaddy
import com.example.ui.theme.AssamTeaLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmarHaatTopBar(
    language: AppLanguage,
    currentRole: UserRole,
    canNavigateBack: Boolean,
    wishlistCount: Int,
    onNavigateBack: () -> Unit,
    onToggleLanguage: () -> Unit,
    onRoleSelected: (UserRole) -> Unit,
    onOpenWishlist: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showRoleMenu by remember { mutableStateOf(false) }

    Surface(
        color = AssamForestGreen,
        contentColor = Color.White,
        shadowElevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (canNavigateBack) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("top_bar_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(AssamGoldenPaddy)
                            .padding(2.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.amar_haat_logo),
                            contentDescription = "Amar Haat Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "আমাৰ হাট অসম" else "Amar Haat Assam",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "পথাৰৰ পৰা প্ৰতিটো ঘৰলৈ" else "From Assam's Farms to Every Home",
                        color = Color(0xFFD4E6D9),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1
                    )
                }

                // Language Toggle Pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0x33FFFFFF),
                    modifier = Modifier
                        .clickable { onToggleLanguage() }
                        .padding(end = 6.dp)
                        .testTag("language_toggle_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Language",
                            tint = AssamGoldenPaddy,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "অসমীয়া" else "ENG",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // User Role Switcher Pill
                Box {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = when (currentRole) {
                            UserRole.BUYER -> Color(0xFF2E7D32)
                            UserRole.SELLER -> Color(0xFFC59B27)
                            UserRole.ADMIN -> Color(0xFF7C3AED)
                        },
                        modifier = Modifier
                            .clickable { showRoleMenu = true }
                            .padding(end = 4.dp)
                            .testTag("user_role_selector")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (currentRole) {
                                    UserRole.BUYER -> Icons.Default.ShoppingCart
                                    UserRole.SELLER -> Icons.Default.Agriculture
                                    UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                                },
                                contentDescription = "Role",
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = when (currentRole) {
                                    UserRole.BUYER -> if (language == AppLanguage.ASSAMESE) "ক্ৰেতা" else "Buyer"
                                    UserRole.SELLER -> if (language == AppLanguage.ASSAMESE) "কৃষক" else "Farmer"
                                    UserRole.ADMIN -> if (language == AppLanguage.ASSAMESE) "প্ৰশাসন" else "Admin"
                                },
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showRoleMenu,
                        onDismissRequest = { showRoleMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(if (language == AppLanguage.ASSAMESE) "🛍️ ক্ৰেতা / দৰ্শক মোড" else "🛍️ Buyer / Visitor Mode")
                            },
                            onClick = {
                                onRoleSelected(UserRole.BUYER)
                                showRoleMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(if (language == AppLanguage.ASSAMESE) "🚜 কৃষক মোড (চতিয়া - প্ৰাঞ্জল)" else "🚜 Farmer Mode (Sootea - Pranjal)")
                            },
                            onClick = {
                                onRoleSelected(UserRole.SELLER)
                                showRoleMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(if (language == AppLanguage.ASSAMESE) "🛡️ বজাৰ প্ৰশাসক (Admin)" else "🛡️ Marketplace Admin")
                            },
                            onClick = {
                                onRoleSelected(UserRole.ADMIN)
                                showRoleMenu = false
                            }
                        )
                    }
                }

                // Wishlist Icon with badge
                IconButton(
                    onClick = onOpenWishlist,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("wishlist_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (wishlistCount > 0) {
                                Badge(
                                    containerColor = AssamGoldenPaddy,
                                    contentColor = Color.Black
                                ) {
                                    Text(wishlistCount.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Saved Items",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
