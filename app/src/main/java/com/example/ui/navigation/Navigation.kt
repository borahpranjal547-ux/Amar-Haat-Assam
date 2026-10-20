package com.example.ui.navigation

sealed class AppScreen {
    object Home : AppScreen()
    object Explore : AppScreen()
    object Farmers : AppScreen()
    object Bulk : AppScreen()
    object SellerDashboard : AppScreen()
    object AdminDashboard : AppScreen()
    object CustomerHub : AppScreen()
    data class ProductDetail(val productId: String) : AppScreen()
    data class ProducerDetail(val sellerId: String) : AppScreen()
}
