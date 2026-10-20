package com.example.data.model

enum class UserRole {
    BUYER,
    SELLER,
    ADMIN
}

enum class SellerType(val labelEn: String, val labelAs: String) {
    FARMER("Individual Farmer", "ব্যক্তিগত কৃষক"),
    SHG("Self-Help Group (SHG)", "আত্মসহায়ক গোট (SHG)"),
    FPO("Farmer Producer Org (FPO)", "কৃষক উৎপাদক সংগঠন (FPO)"),
    ARTISAN("Artisan / Craftsman", "শিল্পী / কাৰিকৰ"),
    LOCAL_MANUFACTURER("Local Manufacturer", "স্থানীয় উৎপাদক"),
    SMALL_BUSINESS("Small Business", "ক্ষুদ্ৰ ব্যৱসায়")
}

enum class AvailabilityStatus(val labelEn: String, val labelAs: String) {
    AVAILABLE_NOW("Available Now", "এতিয়া মজুত আছে"),
    FUTURE_HARVEST("Future Harvest", "ভৱিষ্যত চপোৱা"),
    SEASONAL("Seasonal", "ঋতুভিত্তিক"),
    UNAVAILABLE("Temporarily Unavailable", "সাময়িকভাৱে শেষ")
}

enum class ApprovalStatus(val labelEn: String, val labelAs: String) {
    PENDING("Pending Review", "পৰ্যালোচনাৰ অধীন"),
    APPROVED("Approved", "অনুমোদিত"),
    REJECTED("Rejected", "প্ৰত্যাখ্যাত"),
    SUSPENDED("Suspended", "স্থগিত")
}

enum class PreferredContactMethod(val labelEn: String, val labelAs: String) {
    WHATSAPP("WhatsApp", "হোৱাটছএপ"),
    PHONE("Phone Call", "ফোন কল"),
    BOTH("Phone & WhatsApp", "ফোন আৰু হোৱাটছএপ")
}

enum class EnquiryStatus(val labelEn: String, val labelAs: String) {
    OPEN("Open", "খোলা"),
    RESPONDED("Responded", "উত্তৰ দিয়া হ’ল"),
    RESOLVED("Resolved", "সমাধান হ’ল")
}

enum class BulkStatus(val labelEn: String, val labelAs: String) {
    OPEN("Open Requirement", "খোলা প্ৰয়োজনীয়তা"),
    IN_REVIEW("Connecting Sellers", "বিক্ৰেতাৰ সৈতে সংযোগ"),
    FULFILLED("Fulfilled", "সম্পূৰ্ণ হ’ল")
}
