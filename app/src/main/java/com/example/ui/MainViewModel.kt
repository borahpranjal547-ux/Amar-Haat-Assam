package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.*
import com.example.data.model.*
import com.example.data.repository.MarketplaceRepository
import com.example.ui.localization.AppLanguage
import com.example.ui.navigation.AppScreen
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

enum class SortOrder {
    NEWEST,
    PRICE_LOW_HIGH,
    PRICE_HIGH_LOW
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    val repository = MarketplaceRepository(database)

    // Language state
    private val _currentLanguage = MutableStateFlow(AppLanguage.ASSAMESE)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    // User Role Mode
    private val _currentRole = MutableStateFlow(UserRole.BUYER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    // Current active seller ID when in SELLER mode (Pranjal Saikia from Sootea pilot by default)
    private val _activeSellerId = MutableStateFlow("seller_pranjal")
    val activeSellerId: StateFlow<String> = _activeSellerId.asStateFlow()

    // Navigation Stack
    private val _navigationStack = MutableStateFlow<List<AppScreen>>(listOf(AppScreen.Home))
    val currentScreen: StateFlow<AppScreen> = _navigationStack.map { it.lastOrNull() ?: AppScreen.Home }
        .stateIn(viewModelScope, SharingStarted.Eagerly, AppScreen.Home)

    // Search and Filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedDistrictId = MutableStateFlow<String?>(null)
    val selectedDistrictId: StateFlow<String?> = _selectedDistrictId.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<String?>(null)
    val selectedCategoryId: StateFlow<String?> = _selectedCategoryId.asStateFlow()

    private val _selectedAvailability = MutableStateFlow<String?>(null)
    val selectedAvailability: StateFlow<String?> = _selectedAvailability.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.NEWEST)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    // Feedback Snackbar message
    private val _userFeedbackMessage = MutableStateFlow<String?>(null)
    val userFeedbackMessage: StateFlow<String?> = _userFeedbackMessage.asStateFlow()

    // Flows from DB
    val approvedProducts: StateFlow<List<ProductEntity>> = repository.getApprovedProducts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allProductsAdmin: StateFlow<List<ProductEntity>> = repository.getAllProductsAdmin()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val categories: StateFlow<List<CategoryEntity>> = repository.getCategories()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val districts: StateFlow<List<DistrictEntity>> = repository.getDistricts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val approvedSellers: StateFlow<List<SellerEntity>> = repository.getApprovedSellers()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allSellersAdmin: StateFlow<List<SellerEntity>> = repository.getAllSellersAdmin()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val bulkRequirements: StateFlow<List<BulkRequirementEntity>> = repository.getAllBulkRequirements()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val wishlistIds: StateFlow<List<String>> = repository.getWishlistProductIds("user_default")
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val announcements: StateFlow<List<AnnouncementEntity>> = repository.getActiveAnnouncements()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val currentSellerProducts: StateFlow<List<ProductEntity>> = _activeSellerId
        .flatMapLatest { sellerId -> repository.getProductsBySeller(sellerId) }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val currentSellerEnquiries: StateFlow<List<EnquiryEntity>> = _activeSellerId
        .flatMapLatest { sellerId -> repository.getEnquiriesForSeller(sellerId) }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allEnquiriesAdmin: StateFlow<List<EnquiryEntity>> = repository.getAllEnquiriesAdmin()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    data class FilterCriteria(
        val query: String,
        val districtId: String?,
        val categoryId: String?,
        val availability: String?,
        val sortOrder: SortOrder
    )

    private val filterCriteria = combine(
        _searchQuery,
        _selectedDistrictId,
        _selectedCategoryId,
        _selectedAvailability,
        _sortOrder
    ) { query, districtId, categoryId, availability, sort ->
        FilterCriteria(query, districtId, categoryId, availability, sort)
    }

    // Filtered Products computation
    val filteredProducts: StateFlow<List<ProductEntity>> = combine(
        approvedProducts,
        filterCriteria
    ) { products, criteria ->
        var list = products

        if (criteria.query.isNotBlank()) {
            val q = criteria.query.trim().lowercase()
            list = list.filter {
                it.nameEn.lowercase().contains(q) ||
                it.nameAs.contains(q) ||
                it.descriptionEn.lowercase().contains(q) ||
                it.descriptionAs.contains(q) ||
                it.sellerName.lowercase().contains(q) ||
                it.sellerVillage.lowercase().contains(q) ||
                it.sellerDistrict.lowercase().contains(q) ||
                it.categoryNameEn.lowercase().contains(q)
            }
        }

        if (criteria.districtId != null && criteria.districtId != "all") {
            val dist = districts.value.find { it.id == criteria.districtId }
            if (dist != null) {
                list = list.filter { it.sellerDistrict.equals(dist.nameEn, ignoreCase = true) }
            }
        }

        if (criteria.categoryId != null && criteria.categoryId != "all") {
            list = list.filter { it.categoryId == criteria.categoryId }
        }

        if (criteria.availability != null && criteria.availability != "all") {
            list = list.filter { it.availabilityStatus == criteria.availability }
        }

        when (criteria.sortOrder) {
            SortOrder.NEWEST -> list.sortedByDescending { it.createdAt }
            SortOrder.PRICE_LOW_HIGH -> list.sortedBy { it.price }
            SortOrder.PRICE_HIGH_LOW -> list.sortedByDescending { it.price }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Actions
    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == AppLanguage.ASSAMESE) {
            AppLanguage.ENGLISH
        } else {
            AppLanguage.ASSAMESE
        }
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    fun setUserRole(role: UserRole) {
        _currentRole.value = role
        // Navigate accordingly if needed
        when (role) {
            UserRole.BUYER -> navigateTo(AppScreen.Home)
            UserRole.SELLER -> navigateTo(AppScreen.SellerDashboard)
            UserRole.ADMIN -> navigateTo(AppScreen.AdminDashboard)
        }
    }

    fun navigateTo(screen: AppScreen) {
        _navigationStack.update { it + screen }
    }

    fun navigateBack(): Boolean {
        if (_navigationStack.value.size > 1) {
            _navigationStack.update { it.dropLast(1) }
            return true
        }
        return false
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setDistrictFilter(districtId: String?) {
        _selectedDistrictId.value = districtId
    }

    fun setCategoryFilter(categoryId: String?) {
        _selectedCategoryId.value = categoryId
    }

    fun setAvailabilityFilter(availability: String?) {
        _selectedAvailability.value = availability
    }

    fun setSortOrder(order: SortOrder) {
        _sortOrder.value = order
    }

    fun toggleWishlist(productId: String) {
        viewModelScope.launch {
            repository.toggleWishlist("user_default", productId)
        }
    }

    fun showFeedback(message: String) {
        _userFeedbackMessage.value = message
    }

    fun clearFeedback() {
        _userFeedbackMessage.value = null
    }

    fun submitEnquiry(
        productId: String,
        productName: String,
        sellerId: String,
        buyerName: String,
        buyerPhone: String,
        requestedQty: Double,
        unit: String,
        message: String,
        contactMethod: String
    ) {
        viewModelScope.launch {
            val enquiry = EnquiryEntity(
                id = "enq_${System.currentTimeMillis()}",
                productId = productId,
                productName = productName,
                sellerId = sellerId,
                buyerName = buyerName,
                buyerPhone = buyerPhone,
                requestedQuantity = requestedQty,
                unit = unit,
                message = message,
                preferredContactMethod = contactMethod,
                status = "OPEN"
            )
            repository.submitEnquiry(enquiry)
            val successMsg = if (_currentLanguage.value == AppLanguage.ASSAMESE) {
                "আপোনাৰ অনুসন্ধান বিক্ৰেতালৈ প্ৰেৰণ কৰা হ’ল!"
            } else {
                "Enquiry successfully sent to producer!"
            }
            showFeedback(successMsg)
        }
    }

    fun submitBulkRequirement(
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
    ) {
        viewModelScope.launch {
            val req = BulkRequirementEntity(
                id = "bulk_${System.currentTimeMillis()}",
                buyerName = buyerName,
                businessName = businessName,
                mobileNumber = mobileNumber,
                email = email,
                district = district,
                productRequired = productRequired,
                requiredQuantity = requiredQty,
                unit = unit,
                expectedPrice = expectedPrice,
                requiredDeliveryDate = deliveryDate,
                additionalRequirements = additionalRequirements,
                status = "OPEN"
            )
            repository.submitBulkRequirement(req)
            val msg = if (_currentLanguage.value == AppLanguage.ASSAMESE) {
                "পাইকাৰী প্ৰয়োজনীয়তা সফলতাৰে জমা দিয়া হ’ল!"
            } else {
                "Bulk requirement posted successfully!"
            }
            showFeedback(msg)
        }
    }

    fun registerSeller(
        fullName: String,
        businessName: String,
        sellerType: String,
        village: String,
        district: String,
        mobile: String,
        whatsapp: String,
        email: String,
        descEn: String,
        descAs: String
    ) {
        viewModelScope.launch {
            val id = "seller_${System.currentTimeMillis()}"
            val seller = SellerEntity(
                id = id,
                fullName = fullName,
                farmOrBusinessName = businessName.ifBlank { "$fullName's Farm" },
                sellerType = sellerType,
                village = village,
                district = district,
                mobileNumber = mobile,
                whatsappNumber = whatsapp.ifBlank { mobile },
                email = email,
                descriptionEn = descEn,
                descriptionAs = descAs,
                preferredContactMethod = "BOTH",
                pickupAvailable = true,
                deliveryAvailable = false,
                publicContactVisible = true,
                approvalStatus = "PENDING"
            )
            repository.registerSeller(seller)
            val msg = if (_currentLanguage.value == AppLanguage.ASSAMESE) {
                "পঞ্জীয়ন সম্পন্ন! প্ৰশাসকৰ অনুমোদনৰ পিছত সামগ্ৰী যোগ কৰিব পাৰিব।"
            } else {
                "Registration submitted! Pending admin review before publishing."
            }
            showFeedback(msg)
        }
    }

    fun addProduct(
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
    ) {
        viewModelScope.launch {
            val currentSeller = approvedSellers.value.find { it.id == _activeSellerId.value }
                ?: allSellersAdmin.value.find { it.id == _activeSellerId.value }
            val sellerName = currentSeller?.farmOrBusinessName ?: "Local Producer"
            val village = currentSeller?.village ?: "Sootea"
            val district = currentSeller?.district ?: "Biswanath"
            val phone = currentSeller?.mobileNumber ?: "9435012345"
            val whatsapp = currentSeller?.whatsappNumber ?: "9435012345"

            val product = ProductEntity(
                id = "prod_${System.currentTimeMillis()}",
                sellerId = _activeSellerId.value,
                sellerName = sellerName,
                sellerVillage = village,
                sellerDistrict = district,
                sellerPhone = phone,
                sellerWhatsApp = whatsapp,
                nameEn = nameEn,
                nameAs = nameAs.ifBlank { nameEn },
                categoryId = categoryId,
                categoryNameEn = categoryNameEn,
                categoryNameAs = categoryNameAs,
                descriptionEn = descEn,
                descriptionAs = descAs.ifBlank { descEn },
                imageUrl = "local",
                price = price,
                unit = unit,
                availableQuantity = availableQty,
                minOrderQuantity = minOrderQty,
                availabilityStatus = availabilityStatus,
                harvestDate = harvestDate,
                approvalStatus = "APPROVED", // Auto-approved for pilot seller so it instantly reflects
                pickupAvailable = pickupAvailable,
                deliveryAvailable = deliveryAvailable,
                isOrganicCertified = isOrganic,
                lastConfirmedDate = "Today"
            )
            repository.addProduct(product)
            val msg = if (_currentLanguage.value == AppLanguage.ASSAMESE) {
                "সামগ্ৰী সফলতাৰে তালিকাভুক্ত কৰা হ’ল!"
            } else {
                "Product listed successfully!"
            }
            showFeedback(msg)
        }
    }

    fun updateProductAvailability(productId: String, status: String) {
        viewModelScope.launch {
            repository.updateProductAvailability(productId, status, "Today")
            val msg = if (_currentLanguage.value == AppLanguage.ASSAMESE) {
                "মজুত স্থিতি আপডেট কৰা হ’ল!"
            } else {
                "Stock availability status updated!"
            }
            showFeedback(msg)
        }
    }

    fun updateProductPriceAndStock(productId: String, newPrice: Double, newStock: Double) {
        viewModelScope.launch {
            val existing = approvedProducts.value.find { it.id == productId }
                ?: allProductsAdmin.value.find { it.id == productId }
            if (existing != null) {
                val updated = existing.copy(
                    price = newPrice,
                    availableQuantity = newStock,
                    lastConfirmedDate = "Today"
                )
                repository.updateProduct(updated)
                val msg = if (_currentLanguage.value == AppLanguage.ASSAMESE) {
                    "মূল্য আৰু পৰিমাণ সলনি কৰা হ’ল!"
                } else {
                    "Price and stock updated!"
                }
                showFeedback(msg)
            }
        }
    }

    fun adminApproveProduct(productId: String, approve: Boolean) {
        viewModelScope.launch {
            val newStatus = if (approve) "APPROVED" else "REJECTED"
            repository.updateProductApproval(productId, newStatus)
            showFeedback("Product marked as $newStatus")
        }
    }

    fun adminApproveSeller(sellerId: String, approve: Boolean) {
        viewModelScope.launch {
            val newStatus = if (approve) "APPROVED" else "REJECTED"
            repository.updateSellerApproval(sellerId, newStatus, "20-Oct-2026")
            showFeedback("Seller account marked as $newStatus")
        }
    }

    fun adminUpdateBulkStatus(reqId: String, status: String) {
        viewModelScope.launch {
            repository.updateBulkStatus(reqId, status)
            showFeedback("Bulk requirement updated to $status")
        }
    }

    fun adminUpdateEnquiryStatus(enquiryId: String, status: String) {
        viewModelScope.launch {
            repository.updateEnquiryStatus(enquiryId, status)
            showFeedback("Enquiry updated to $status")
        }
    }
}
