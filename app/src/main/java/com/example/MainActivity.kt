package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.ProductEntity
import com.example.data.model.UserRole
import com.example.ui.MainViewModel
import com.example.ui.components.*
import com.example.ui.localization.AppLanguage
import com.example.ui.navigation.AppScreen
import com.example.ui.screens.*
import com.example.ui.theme.AssamForestGreen
import com.example.ui.theme.AssamGoldenPaddy
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AmarHaatApp()
            }
        }
    }
}

@Composable
fun AmarHaatApp(viewModel: MainViewModel = viewModel()) {
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val userFeedback by viewModel.userFeedbackMessage.collectAsStateWithLifecycle()

    val approvedProducts by viewModel.approvedProducts.collectAsStateWithLifecycle()
    val filteredProducts by viewModel.filteredProducts.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val districts by viewModel.districts.collectAsStateWithLifecycle()
    val sellers by viewModel.approvedSellers.collectAsStateWithLifecycle()
    val allSellersAdmin by viewModel.allSellersAdmin.collectAsStateWithLifecycle()
    val allProductsAdmin by viewModel.allProductsAdmin.collectAsStateWithLifecycle()
    val currentSellerProducts by viewModel.currentSellerProducts.collectAsStateWithLifecycle()
    val currentSellerEnquiries by viewModel.currentSellerEnquiries.collectAsStateWithLifecycle()
    val allEnquiriesAdmin by viewModel.allEnquiriesAdmin.collectAsStateWithLifecycle()
    val bulkRequirements by viewModel.bulkRequirements.collectAsStateWithLifecycle()
    val wishlistIds by viewModel.wishlistIds.collectAsStateWithLifecycle()
    val announcements by viewModel.announcements.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedDistrictId by viewModel.selectedDistrictId.collectAsStateWithLifecycle()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsStateWithLifecycle()
    val selectedAvailability by viewModel.selectedAvailability.collectAsStateWithLifecycle()
    val sortOrder by viewModel.sortOrder.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    // Dialog States
    var enquiryProductTarget by remember { mutableStateOf<ProductEntity?>(null) }
    var showSellerRegistration by remember { mutableStateOf(false) }

    // Handle feedback snackbars
    LaunchedEffect(userFeedback) {
        userFeedback?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearFeedback()
        }
    }

    // Handle back press
    BackHandler(enabled = currentScreen !is AppScreen.Home) {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                AmarHaatTopBar(
                    language = language,
                    currentRole = currentRole,
                    canNavigateBack = currentScreen !is AppScreen.Home,
                    wishlistCount = wishlistIds.size,
                    onNavigateBack = { viewModel.navigateBack() },
                    onToggleLanguage = { viewModel.toggleLanguage() },
                    onRoleSelected = { role -> viewModel.setUserRole(role) },
                    onOpenWishlist = { viewModel.navigateTo(AppScreen.CustomerHub) }
                )

                if (currentScreen is AppScreen.Home) {
                    AnnouncementBar(
                        announcements = announcements,
                        language = language,
                        onAnnouncementClick = { viewModel.navigateTo(AppScreen.Explore) }
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = currentScreen is AppScreen.Home,
                    onClick = { viewModel.navigateTo(AppScreen.Home) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text(if (language == AppLanguage.ASSAMESE) "ঘৰ" else "Home", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AssamForestGreen,
                        selectedTextColor = AssamForestGreen,
                        indicatorColor = Color(0xFFE8F5E9)
                    ),
                    modifier = Modifier.testTag("nav_tab_home")
                )

                NavigationBarItem(
                    selected = currentScreen is AppScreen.Explore,
                    onClick = { viewModel.navigateTo(AppScreen.Explore) },
                    icon = { Icon(Icons.Default.Search, contentDescription = "Explore") },
                    label = { Text(if (language == AppLanguage.ASSAMESE) "সামগ্ৰী" else "Explore", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AssamForestGreen,
                        selectedTextColor = AssamForestGreen,
                        indicatorColor = Color(0xFFE8F5E9)
                    ),
                    modifier = Modifier.testTag("nav_tab_explore")
                )

                NavigationBarItem(
                    selected = currentScreen is AppScreen.Farmers,
                    onClick = { viewModel.navigateTo(AppScreen.Farmers) },
                    icon = { Icon(Icons.Default.Agriculture, contentDescription = "Producers") },
                    label = { Text(if (language == AppLanguage.ASSAMESE) "উৎপাদক" else "Producers", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AssamForestGreen,
                        selectedTextColor = AssamForestGreen,
                        indicatorColor = Color(0xFFE8F5E9)
                    ),
                    modifier = Modifier.testTag("nav_tab_farmers")
                )

                NavigationBarItem(
                    selected = currentScreen is AppScreen.Bulk,
                    onClick = { viewModel.navigateTo(AppScreen.Bulk) },
                    icon = { Icon(Icons.Default.LocalShipping, contentDescription = "Bulk") },
                    label = { Text(if (language == AppLanguage.ASSAMESE) "পাইকাৰী" else "Bulk", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AssamForestGreen,
                        selectedTextColor = AssamForestGreen,
                        indicatorColor = Color(0xFFE8F5E9)
                    ),
                    modifier = Modifier.testTag("nav_tab_bulk")
                )

                val accountLabel = when (currentRole) {
                    UserRole.BUYER -> if (language == AppLanguage.ASSAMESE) "একাউণ্ট" else "Account"
                    UserRole.SELLER -> if (language == AppLanguage.ASSAMESE) "কৃষক হাব" else "Farmer"
                    UserRole.ADMIN -> if (language == AppLanguage.ASSAMESE) "প্ৰশাসন" else "Admin"
                }

                val isAccountActive = currentScreen is AppScreen.CustomerHub ||
                        currentScreen is AppScreen.SellerDashboard ||
                        currentScreen is AppScreen.AdminDashboard

                NavigationBarItem(
                    selected = isAccountActive,
                    onClick = {
                        when (currentRole) {
                            UserRole.BUYER -> viewModel.navigateTo(AppScreen.CustomerHub)
                            UserRole.SELLER -> viewModel.navigateTo(AppScreen.SellerDashboard)
                            UserRole.ADMIN -> viewModel.navigateTo(AppScreen.AdminDashboard)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = when (currentRole) {
                                UserRole.BUYER -> Icons.Default.Person
                                UserRole.SELLER -> Icons.Default.Dashboard
                                UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                            },
                            contentDescription = "Account"
                        )
                    },
                    label = { Text(accountLabel, fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AssamForestGreen,
                        selectedTextColor = AssamForestGreen,
                        indicatorColor = Color(0xFFE8F5E9)
                    ),
                    modifier = Modifier.testTag("nav_tab_account")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is AppScreen.Home -> {
                    HomeScreen(
                        products = approvedProducts,
                        categories = categories,
                        districts = districts,
                        sellers = sellers,
                        wishlistIds = wishlistIds,
                        language = language,
                        onSearchSubmit = { q ->
                            viewModel.setSearchQuery(q)
                            viewModel.navigateTo(AppScreen.Explore)
                        },
                        onCategorySelected = { catId ->
                            viewModel.setCategoryFilter(catId)
                            viewModel.navigateTo(AppScreen.Explore)
                        },
                        onDistrictSelected = { distId ->
                            viewModel.setDistrictFilter(distId)
                        },
                        onProductClick = { prodId ->
                            viewModel.navigateTo(AppScreen.ProductDetail(prodId))
                        },
                        onContactSeller = { prod ->
                            enquiryProductTarget = prod
                        },
                        onToggleWishlist = { prodId ->
                            viewModel.toggleWishlist(prodId)
                        },
                        onNavigateToExplore = {
                            viewModel.navigateTo(AppScreen.Explore)
                        },
                        onNavigateToFarmers = {
                            viewModel.navigateTo(AppScreen.Farmers)
                        },
                        onNavigateToBulk = {
                            viewModel.navigateTo(AppScreen.Bulk)
                        },
                        onRegisterSellerClick = {
                            showSellerRegistration = true
                        }
                    )
                }

                is AppScreen.Explore -> {
                    ExploreProductsScreen(
                        products = filteredProducts,
                        categories = categories,
                        districts = districts,
                        wishlistIds = wishlistIds,
                        searchQuery = searchQuery,
                        selectedDistrictId = selectedDistrictId,
                        selectedCategoryId = selectedCategoryId,
                        selectedAvailability = selectedAvailability,
                        sortOrder = sortOrder,
                        language = language,
                        onSearchChange = { viewModel.setSearchQuery(it) },
                        onDistrictChange = { viewModel.setDistrictFilter(it) },
                        onCategoryChange = { viewModel.setCategoryFilter(it) },
                        onAvailabilityChange = { viewModel.setAvailabilityFilter(it) },
                        onSortChange = { viewModel.setSortOrder(it) },
                        onProductClick = { prodId ->
                            viewModel.navigateTo(AppScreen.ProductDetail(prodId))
                        },
                        onContactSeller = { prod ->
                            enquiryProductTarget = prod
                        },
                        onToggleWishlist = { prodId ->
                            viewModel.toggleWishlist(prodId)
                        }
                    )
                }

                is AppScreen.Farmers -> {
                    FarmersListScreen(
                        sellers = sellers,
                        districts = districts,
                        language = language,
                        onViewSellerProducts = { seller ->
                            viewModel.setSearchQuery(seller.farmOrBusinessName)
                            viewModel.navigateTo(AppScreen.Explore)
                        },
                        onRegisterSellerClick = {
                            showSellerRegistration = true
                        }
                    )
                }

                is AppScreen.Bulk -> {
                    BulkRequirementScreen(
                        requirements = bulkRequirements,
                        districts = districts,
                        language = language,
                        onSubmitBulkRequirement = { name, biz, phone, email, dist, prod, qty, unit, price, date, notes ->
                            viewModel.submitBulkRequirement(name, biz, phone, email, dist, prod, qty, unit, price, date, notes)
                        }
                    )
                }

                is AppScreen.ProductDetail -> {
                    val product = approvedProducts.find { it.id == screen.productId }
                        ?: allProductsAdmin.find { it.id == screen.productId }
                    ProductDetailScreen(
                        product = product,
                        language = language,
                        isWishlisted = wishlistIds.contains(screen.productId),
                        onBackClick = { viewModel.navigateBack() },
                        onSendEnquiry = { prod -> enquiryProductTarget = prod },
                        onToggleWishlist = { viewModel.toggleWishlist(screen.productId) },
                        onReportListing = {
                            viewModel.showFeedback("Report submitted for administrator review.")
                        }
                    )
                }

                is AppScreen.ProducerDetail -> {
                    // Navigate to explore filtered by producer
                    val seller = sellers.find { it.id == screen.sellerId }
                    if (seller != null) {
                        viewModel.setSearchQuery(seller.farmOrBusinessName)
                        viewModel.navigateTo(AppScreen.Explore)
                    }
                }

                is AppScreen.SellerDashboard -> {
                    val activeSeller = sellers.find { it.id == viewModel.activeSellerId.value }
                        ?: allSellersAdmin.find { it.id == viewModel.activeSellerId.value }
                    FarmerDashboardScreen(
                        currentSeller = activeSeller,
                        sellerProducts = currentSellerProducts,
                        sellerEnquiries = currentSellerEnquiries,
                        categories = categories,
                        language = language,
                        onAddProduct = { nEn, nAs, catId, cEn, cAs, dEn, dAs, p, u, q, minQ, st, hDate, pu, del, org ->
                            viewModel.addProduct(nEn, nAs, catId, cEn, cAs, dEn, dAs, p, u, q, minQ, st, hDate, pu, del, org)
                        },
                        onToggleAvailability = { id, currentStatus ->
                            viewModel.updateProductAvailability(id, currentStatus)
                        },
                        onUpdatePriceStock = { id, newP, newS ->
                            viewModel.updateProductPriceAndStock(id, newP, newS)
                        }
                    )
                }

                is AppScreen.AdminDashboard -> {
                    AdminDashboardScreen(
                        allProducts = allProductsAdmin,
                        allSellers = allSellersAdmin,
                        allEnquiries = allEnquiriesAdmin,
                        allBulkRequests = bulkRequirements,
                        language = language,
                        onApproveProduct = { id, app -> viewModel.adminApproveProduct(id, app) },
                        onApproveSeller = { id, app -> viewModel.adminApproveSeller(id, app) },
                        onUpdateBulkStatus = { id, st -> viewModel.adminUpdateBulkStatus(id, st) }
                    )
                }

                is AppScreen.CustomerHub -> {
                    val wishlisted = approvedProducts.filter { wishlistIds.contains(it.id) }
                    CustomerHubScreen(
                        wishlistedProducts = wishlisted,
                        buyerEnquiries = emptyList(),
                        wishlistIds = wishlistIds,
                        language = language,
                        onProductClick = { prodId -> viewModel.navigateTo(AppScreen.ProductDetail(prodId)) },
                        onContactSeller = { prod -> enquiryProductTarget = prod },
                        onToggleWishlist = { prodId -> viewModel.toggleWishlist(prodId) },
                        onSwitchToSellerMode = { viewModel.setUserRole(UserRole.SELLER) }
                    )
                }
            }
        }
    }

    // Direct Enquiry Modal
    enquiryProductTarget?.let { prod ->
        EnquiryDialog(
            product = prod,
            language = language,
            onDismiss = { enquiryProductTarget = null },
            onSubmit = { bName, bPhone, qty, unit, msg, method ->
                viewModel.submitEnquiry(
                    productId = prod.id,
                    productName = prod.nameEn,
                    sellerId = prod.sellerId,
                    buyerName = bName,
                    buyerPhone = bPhone,
                    requestedQty = qty,
                    unit = unit,
                    message = msg,
                    contactMethod = method
                )
            }
        )
    }

    // Seller Registration Dialog
    if (showSellerRegistration) {
        SellerRegistrationDialog(
            districts = districts,
            language = language,
            onDismiss = { showSellerRegistration = false },
            onSubmit = { name, biz, type, vill, dist, mob, wa, em, dEn, dAs ->
                viewModel.registerSeller(name, biz, type, vill, dist, mob, wa, em, dEn, dAs)
            }
        )
    }
}
