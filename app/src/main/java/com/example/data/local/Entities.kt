package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val sellerId: String,
    val sellerName: String,
    val sellerVillage: String,
    val sellerDistrict: String,
    val sellerPhone: String,
    val sellerWhatsApp: String,
    val nameEn: String,
    val nameAs: String,
    val categoryId: String,
    val categoryNameEn: String,
    val categoryNameAs: String,
    val descriptionEn: String,
    val descriptionAs: String,
    val imageUrl: String,
    val price: Double,
    val unit: String, // kg, quintal, litre, packet, piece, bundle, dozen
    val availableQuantity: Double,
    val minOrderQuantity: Double = 1.0,
    val availabilityStatus: String, // AVAILABLE_NOW, FUTURE_HARVEST, SEASONAL, UNAVAILABLE
    val harvestDate: String = "",
    val approvalStatus: String = "APPROVED", // PENDING, APPROVED, REJECTED
    val pickupAvailable: Boolean = true,
    val deliveryAvailable: Boolean = false,
    val isOrganicCertified: Boolean = false,
    val lastConfirmedDate: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val nameEn: String,
    val nameAs: String,
    val iconName: String,
    val displayOrder: Int = 0,
    val isActive: Boolean = true
)

@Entity(tableName = "districts")
data class DistrictEntity(
    @PrimaryKey
    val id: String,
    val nameEn: String,
    val nameAs: String,
    val isPilotZone: Boolean = false,
    val keyVillages: String = "" // comma separated (e.g., Sootea, Ambari, Biswanath Chariali)
)

@Entity(tableName = "sellers")
data class SellerEntity(
    @PrimaryKey
    val id: String,
    val fullName: String,
    val farmOrBusinessName: String,
    val sellerType: String, // FARMER, SHG, FPO, ARTISAN, LOCAL_MANUFACTURER, SMALL_BUSINESS
    val village: String,
    val district: String,
    val mobileNumber: String,
    val whatsappNumber: String,
    val email: String = "",
    val descriptionEn: String,
    val descriptionAs: String,
    val preferredContactMethod: String = "BOTH",
    val pickupAvailable: Boolean = true,
    val deliveryAvailable: Boolean = false,
    val publicContactVisible: Boolean = true,
    val approvalStatus: String = "APPROVED", // PENDING, APPROVED, REJECTED, SUSPENDED
    val verificationDate: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "enquiries")
data class EnquiryEntity(
    @PrimaryKey
    val id: String,
    val productId: String,
    val productName: String,
    val sellerId: String,
    val buyerName: String,
    val buyerPhone: String,
    val requestedQuantity: Double,
    val unit: String,
    val message: String,
    val preferredContactMethod: String = "PHONE",
    val status: String = "OPEN", // OPEN, RESPONDED, RESOLVED
    val adminNotes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "bulk_requirements")
data class BulkRequirementEntity(
    @PrimaryKey
    val id: String,
    val buyerName: String,
    val businessName: String = "",
    val mobileNumber: String,
    val email: String = "",
    val district: String,
    val productRequired: String,
    val requiredQuantity: Double,
    val unit: String,
    val expectedPrice: String = "",
    val requiredDeliveryDate: String = "",
    val additionalRequirements: String = "",
    val status: String = "OPEN", // OPEN, IN_REVIEW, FULFILLED
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "wishlist")
data class WishlistEntity(
    @PrimaryKey
    val id: String, // composite or uuid
    val userId: String = "user_default",
    val productId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey
    val id: String,
    val textEn: String,
    val textAs: String,
    val isActive: Boolean = true,
    val displayOrder: Int = 0
)
