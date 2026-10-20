package com.example.data.repository

import com.example.data.local.*
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class MarketplaceRepository(private val db: AppDatabase) {

    private val productDao = db.productDao()
    private val categoryDao = db.categoryDao()
    private val districtDao = db.districtDao()
    private val sellerDao = db.sellerDao()
    private val enquiryDao = db.enquiryDao()
    private val bulkDao = db.bulkRequirementDao()
    private val wishlistDao = db.wishlistDao()
    private val announcementDao = db.announcementDao()

    // Products
    fun getApprovedProducts(): Flow<List<ProductEntity>> = productDao.getAllApprovedProducts()

    fun getAllProductsAdmin(): Flow<List<ProductEntity>> = productDao.getAllProductsAdmin()

    fun getProductsBySeller(sellerId: String): Flow<List<ProductEntity>> =
        productDao.getProductsBySeller(sellerId)

    fun getProductById(id: String): Flow<ProductEntity?> = productDao.getProductById(id)

    suspend fun addProduct(product: ProductEntity) {
        productDao.insertProduct(product)
    }

    suspend fun updateProduct(product: ProductEntity) {
        productDao.updateProduct(product)
    }

    suspend fun updateProductAvailability(id: String, status: String, confirmedDate: String) {
        productDao.updateAvailability(id, status, confirmedDate)
    }

    suspend fun updateProductApproval(id: String, status: String) {
        productDao.updateApprovalStatus(id, status)
    }

    suspend fun deleteProduct(id: String) {
        productDao.deleteProduct(id)
    }

    // Categories
    fun getCategories(): Flow<List<CategoryEntity>> = categoryDao.getAllCategories()

    suspend fun addCategory(category: CategoryEntity) {
        categoryDao.insertCategory(category)
    }

    // Districts
    fun getDistricts(): Flow<List<DistrictEntity>> = districtDao.getAllDistricts()

    // Sellers
    fun getApprovedSellers(): Flow<List<SellerEntity>> = sellerDao.getAllApprovedSellers()

    fun getAllSellersAdmin(): Flow<List<SellerEntity>> = sellerDao.getAllSellersAdmin()

    fun getSellerById(id: String): Flow<SellerEntity?> = sellerDao.getSellerById(id)

    suspend fun registerSeller(seller: SellerEntity) {
        sellerDao.insertSeller(seller)
    }

    suspend fun updateSellerApproval(id: String, status: String, verificationDate: String) {
        sellerDao.updateSellerStatus(id, status, verificationDate)
    }

    // Enquiries
    fun getEnquiriesForSeller(sellerId: String): Flow<List<EnquiryEntity>> =
        enquiryDao.getEnquiriesForSeller(sellerId)

    fun getAllEnquiriesAdmin(): Flow<List<EnquiryEntity>> = enquiryDao.getAllEnquiriesAdmin()

    fun getEnquiriesForBuyer(phone: String): Flow<List<EnquiryEntity>> =
        enquiryDao.getEnquiriesByBuyer(phone)

    suspend fun submitEnquiry(enquiry: EnquiryEntity) {
        enquiryDao.insertEnquiry(enquiry)
    }

    suspend fun updateEnquiryStatus(id: String, status: String) {
        enquiryDao.updateEnquiryStatus(id, status)
    }

    // Bulk Requirements
    fun getAllBulkRequirements(): Flow<List<BulkRequirementEntity>> =
        bulkDao.getAllBulkRequirements()

    suspend fun submitBulkRequirement(requirement: BulkRequirementEntity) {
        bulkDao.insertBulkRequirement(requirement)
    }

    suspend fun updateBulkStatus(id: String, status: String) {
        bulkDao.updateBulkStatus(id, status)
    }

    // Wishlist
    fun getWishlistProductIds(userId: String = "user_default"): Flow<List<String>> =
        wishlistDao.getWishlistProductIds(userId)

    suspend fun toggleWishlist(userId: String = "user_default", productId: String) {
        if (wishlistDao.isInWishlist(userId, productId)) {
            wishlistDao.removeFromWishlist(userId, productId)
        } else {
            wishlistDao.addToWishlist(
                WishlistEntity(
                    id = "${userId}_$productId",
                    userId = userId,
                    productId = productId
                )
            )
        }
    }

    // Announcements
    fun getActiveAnnouncements(): Flow<List<AnnouncementEntity>> =
        announcementDao.getActiveAnnouncements()
}
