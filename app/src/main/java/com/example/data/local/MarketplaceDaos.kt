package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE approvalStatus = 'APPROVED' ORDER BY createdAt DESC")
    fun getAllApprovedProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products ORDER BY createdAt DESC")
    fun getAllProductsAdmin(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE sellerId = :sellerId ORDER BY createdAt DESC")
    fun getProductsBySeller(sellerId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductById(id: String): Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductByIdSync(id: String): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("UPDATE products SET availabilityStatus = :status, lastConfirmedDate = :lastConfirmed WHERE id = :id")
    suspend fun updateAvailability(id: String, status: String, lastConfirmed: String)

    @Query("UPDATE products SET approvalStatus = :status WHERE id = :id")
    suspend fun updateApprovalStatus(id: String, status: String)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProduct(id: String)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int
}

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories WHERE isActive = 1 ORDER BY displayOrder ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT COUNT(*) FROM categories")
    suspend fun getCount(): Int
}

@Dao
interface DistrictDao {
    @Query("SELECT * FROM districts ORDER BY isPilotZone DESC, nameEn ASC")
    fun getAllDistricts(): Flow<List<DistrictEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistricts(districts: List<DistrictEntity>)

    @Query("SELECT COUNT(*) FROM districts")
    suspend fun getCount(): Int
}

@Dao
interface SellerDao {
    @Query("SELECT * FROM sellers WHERE approvalStatus = 'APPROVED' ORDER BY fullName ASC")
    fun getAllApprovedSellers(): Flow<List<SellerEntity>>

    @Query("SELECT * FROM sellers ORDER BY createdAt DESC")
    fun getAllSellersAdmin(): Flow<List<SellerEntity>>

    @Query("SELECT * FROM sellers WHERE id = :id")
    fun getSellerById(id: String): Flow<SellerEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeller(seller: SellerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSellers(sellers: List<SellerEntity>)

    @Query("UPDATE sellers SET approvalStatus = :status, verificationDate = :verDate WHERE id = :id")
    suspend fun updateSellerStatus(id: String, status: String, verDate: String)

    @Query("SELECT COUNT(*) FROM sellers")
    suspend fun getCount(): Int
}

@Dao
interface EnquiryDao {
    @Query("SELECT * FROM enquiries WHERE sellerId = :sellerId ORDER BY createdAt DESC")
    fun getEnquiriesForSeller(sellerId: String): Flow<List<EnquiryEntity>>

    @Query("SELECT * FROM enquiries ORDER BY createdAt DESC")
    fun getAllEnquiriesAdmin(): Flow<List<EnquiryEntity>>

    @Query("SELECT * FROM enquiries WHERE buyerPhone = :phone ORDER BY createdAt DESC")
    fun getEnquiriesByBuyer(phone: String): Flow<List<EnquiryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnquiry(enquiry: EnquiryEntity)

    @Query("UPDATE enquiries SET status = :status WHERE id = :id")
    suspend fun updateEnquiryStatus(id: String, status: String)
}

@Dao
interface BulkRequirementDao {
    @Query("SELECT * FROM bulk_requirements ORDER BY createdAt DESC")
    fun getAllBulkRequirements(): Flow<List<BulkRequirementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBulkRequirement(requirement: BulkRequirementEntity)

    @Query("UPDATE bulk_requirements SET status = :status WHERE id = :id")
    suspend fun updateBulkStatus(id: String, status: String)
}

@Dao
interface WishlistDao {
    @Query("SELECT productId FROM wishlist WHERE userId = :userId")
    fun getWishlistProductIds(userId: String): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(item: WishlistEntity)

    @Query("DELETE FROM wishlist WHERE userId = :userId AND productId = :productId")
    suspend fun removeFromWishlist(userId: String, productId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE userId = :userId AND productId = :productId)")
    suspend fun isInWishlist(userId: String, productId: String): Boolean
}

@Dao
interface AnnouncementDao {
    @Query("SELECT * FROM announcements WHERE isActive = 1 ORDER BY displayOrder ASC")
    fun getActiveAnnouncements(): Flow<List<AnnouncementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncements(list: List<AnnouncementEntity>)

    @Query("SELECT COUNT(*) FROM announcements")
    suspend fun getCount(): Int
}
