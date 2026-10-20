package com.example.ui.localization

enum class AppLanguage(val code: String, val displayName: String, val scriptName: String) {
    ASSAMESE("as", "অসমীয়া", "অসমীয়া লিপি"),
    ENGLISH("en", "English", "English")
}

object AppStrings {
    fun get(key: String, language: AppLanguage): String {
        val entry = strings[key] ?: return key
        return if (language == AppLanguage.ASSAMESE) entry.asString else entry.enString
    }

    private data class Trans(val enString: String, val asString: String)

    private val strings = mapOf(
        "app_title" to Trans("Amar Haat Assam", "আমাৰ হাট অসম"),
        "app_tagline" to Trans("From Assam’s Farms to Every Home", "অসমৰ পথাৰৰ পৰা প্ৰতিটো ঘৰলৈ"),
        "hero_headline" to Trans("Discover the Best of Assam, Direct from Local Producers", "স্থানীয় উৎপাদকৰ পৰা পোনপটীয়াকৈ অসমৰ শ্ৰেষ্ঠ সামগ্ৰী আৱিষ্কাৰ কৰক"),
        "hero_subtext" to Trans("Explore fresh agricultural products, traditional Assamese food, handmade crafts, and local products from farmers and small businesses across Assam.", "অসমৰ কৃষক আৰু ক্ষুদ্ৰ ব্যৱসায়ীৰ পৰা সতেজ কৃষি সামগ্ৰী, পৰম্পৰাগত খাদ্য আৰু হস্তশিল্প বিচাৰি লওক।"),
        "nav_home" to Trans("Home", "ঘৰ"),
        "nav_products" to Trans("Explore", "সামগ্ৰী"),
        "nav_farmers" to Trans("Producers", "উৎপাদক"),
        "nav_bulk" to Trans("Bulk Orders", "পাইকাৰী"),
        "nav_account" to Trans("Account", "একাউণ্ট"),
        "search_hint" to Trans("Search rice, tea, vegetables, crafts...", "চাউল, চাহ, শাক-পাচলি, জাপি বিচাৰক..."),
        "select_district" to Trans("All Assam Districts", "সকলো জিলা"),
        "pilot_badge" to Trans("Pilot Focus: Sootea • Ambari • Biswanath", "পাইলট অঞ্চল: চতিয়া • আমবাৰী • বিশ্বনাথ"),
        "explore_categories" to Trans("Product Categories", "সামগ্ৰীৰ শ্ৰেণীসমূহ"),
        "featured_products" to Trans("Featured Local Products", "বাছকবনীয়া থলুৱা সামগ্ৰী"),
        "view_all" to Trans("View All", "সকলো চাওক"),
        "available_now" to Trans("Available Now", "এতিয়া মজুত আছে"),
        "future_harvest" to Trans("Future Harvest", "ভৱিষ্যত চপোৱা"),
        "seasonal" to Trans("Seasonal", "ঋতুভিত্তিক"),
        "unavailable" to Trans("Unavailable", "সাময়িকভাৱে শেষ"),
        "organic_certified" to Trans("Organic Certified", "জৈৱিক প্ৰমাণিত"),
        "contact_seller" to Trans("Contact Seller", "বিক্ৰেতাৰ সৈতে যোগাযোগ"),
        "send_enquiry" to Trans("Send Direct Enquiry", "অনুসন্ধান পঠিয়াওক"),
        "how_it_works" to Trans("How Amar Haat Works", "আমাৰ হাটে কেনেদৰে কাম কৰে"),
        "for_buyers" to Trans("For Buyers", "ক্ৰেতাৰ বাবে"),
        "for_sellers" to Trans("For Sellers", "বিক্ৰেতাৰ বাবে"),
        "buyer_step1" to Trans("1. Browse local products and farmers in your district", "১. আপোনাৰ জিলাৰ থলুৱা সামগ্ৰী আৰু কৃষক বিচাৰক"),
        "buyer_step2" to Trans("2. Compare transparent seller-listed prices and quantities", "২. বিক্ৰেতাই উল্লেখ কৰা মূল্য আৰু পৰিমাণ পৰীক্ষা কৰক"),
        "buyer_step3" to Trans("3. Contact producer directly via WhatsApp or phone call", "৩. হোৱাটছএপ বা ফোনেৰে পোনপটীয়াকৈ যোগাযোগ কৰক"),
        "buyer_step4" to Trans("4. Agree on pickup or delivery directly without extra fees", "৪. কোনো অতিৰিক্ত মাচুল অবিহনে সামগ্ৰী সংগ্ৰহ কৰক"),
        "seller_step1" to Trans("1. Register easily with basic farm details", "১. ফাৰ্মৰ সহজ বিৱৰণেৰে বিনামূলীয়াকৈ পঞ্জীয়ন কৰক"),
        "seller_step2" to Trans("2. Post products with price, available stock & harvest date", "২. মূল্য, মজুত পৰিমাণ আৰু চপোৱা তাৰিখ উল্লেখ কৰি সামগ্ৰী যোগ দিয়ক"),
        "seller_step3" to Trans("3. Admin approves to maintain quality and transparency", "৩. গুণমান নিশ্চিত কৰিবলৈ প্ৰশাসকে অনুমোদন জনায়"),
        "seller_step4" to Trans("4. Receive genuine inquiries directly from local buyers", "৪. স্থানীয় ক্ৰেতাসকলৰ পৰা পোনপটীয়া অনুসন্ধান লাভ কৰক"),
        "bulk_section_title" to Trans("Bulk Buyer Connect", "পাইকাৰী ক্ৰেতাৰ সুবিধা"),
        "bulk_section_sub" to Trans("For retail shops, hotels, restaurants, grocery stores & wholesalers seeking bulk farm produce.", "দোকান, হোটেল, ৰেষ্টুৰেণ্ট আৰু পাইকাৰী ব্যৱসায়ীৰ বাবে পোনপটীয়া কৃষি সামগ্ৰী।"),
        "post_bulk_btn" to Trans("Post Bulk Requirement", "পাইকাৰী প্ৰয়োজনীয়তা জমা দিয়ক"),
        "impact_title" to Trans("Community Impact", "সামাজিক প্ৰভাৱ"),
        "impact_sub" to Trans("Amar Haat is an independent local initiative to empower rural Assamese producers with direct digital visibility.", "আমাৰ হাট অসমৰ গ্ৰাম্য উৎপাদকক পোনপটীয়া ডিজিটেল সংযোগ প্ৰদানৰ এক স্বতন্ত্ৰ পদক্ষেপ।"),
        "admin_disclaimer" to Trans("Amar Haat Assam is an independent community marketplace and is not an official Assam Government portal.", "আমাৰ হাট অসম এখন স্বতন্ত্ৰ সম্প্ৰদায় বজাৰ আৰু অসম চৰকাৰৰ চৰকাৰী পৰ্টেল নহয়।"),
        "seller_dashboard" to Trans("Seller Dashboard", "বিক্ৰেতাৰ ডেচবৰ্ড"),
        "customer_dashboard" to Trans("My Customer Hub", "মোৰ একাউণ্ট"),
        "admin_dashboard" to Trans("Marketplace Admin", "প্ৰশাসন পেনেল"),
        "add_product" to Trans("Add New Product", "নতুন সামগ্ৰী যোগ কৰক"),
        "switch_mode" to Trans("Switch User Mode", "ব্যৱহাৰকাৰীৰ ভূমিকা সলনি কৰক"),
        "mode_buyer" to Trans("Browse as Buyer / Visitor", "ক্ৰেতা হিচাপে ব্ৰাউজ কৰক"),
        "mode_seller" to Trans("Farmer Mode (Pranjal - Sootea)", "কৃষক মোড (প্ৰাঞ্জল - চতিয়া)"),
        "mode_admin" to Trans("Marketplace Administrator", "বজাৰ প্ৰশাসক"),
        "filter_by_cat" to Trans("Filter by Category", "শ্ৰেণী অনুসৰি বাছক"),
        "filter_by_dist" to Trans("Filter by District", "জিলা অনুসৰি বাছক"),
        "sort_price_low" to Trans("Price: Low to High", "মূল্য: কমৰ পৰা বেছি"),
        "sort_price_high" to Trans("Price: High to Low", "মূল্য: বেছিৰ পৰা কম"),
        "sort_newest" to Trans("Recently Listed", "নতুনকৈ যোগ কৰা"),
        "enquiry_submitted" to Trans("Enquiry sent successfully to seller!", "বিক্ৰেতালৈ অনুসন্ধান সফলভাৱে প্ৰেৰণ কৰা হ’ল!"),
        "call_producer" to Trans("Call Producer", "ফোন কৰক"),
        "whatsapp_producer" to Trans("WhatsApp Producer", "হোৱাটছএপ কৰক")
    )
}
