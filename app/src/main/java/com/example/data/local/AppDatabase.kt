package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProductEntity::class,
        CategoryEntity::class,
        DistrictEntity::class,
        SellerEntity::class,
        EnquiryEntity::class,
        BulkRequirementEntity::class,
        WishlistEntity::class,
        AnnouncementEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun districtDao(): DistrictDao
    abstract fun sellerDao(): SellerDao
    abstract fun enquiryDao(): EnquiryDao
    abstract fun bulkRequirementDao(): BulkRequirementDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun announcementDao(): AnnouncementDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "amar_haat_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database)
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        if (database.productDao().getProductCount() == 0) {
                            populateDatabase(database)
                        }
                    }
                }
            }

            private suspend fun populateDatabase(database: AppDatabase) {
                database.categoryDao().insertCategories(InitialData.categories)
                database.districtDao().insertDistricts(InitialData.districts)
                database.sellerDao().insertSellers(InitialData.sellers)
                database.productDao().insertProducts(InitialData.products)
                database.announcementDao().insertAnnouncements(InitialData.announcements)
            }
        }
    }
}
