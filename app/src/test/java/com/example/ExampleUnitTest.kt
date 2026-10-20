package com.example

import com.example.data.local.InitialData
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.AppStrings
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testInitialDataCategoriesCount() {
        assertEquals(12, InitialData.categories.size)
    }

    @Test
    fun testPilotZoneBiswanathPresent() {
        val biswanath = InitialData.districts.find { it.id == "dist_biswanath" }
        assertNotNull(biswanath)
        assertTrue(biswanath!!.isPilotZone)
        assertTrue(biswanath.keyVillages.contains("Sootea"))
        assertTrue(biswanath.keyVillages.contains("Ambari"))
    }

    @Test
    fun testPilotProductsExist() {
        val johaRice = InitialData.products.find { it.id == "prod_joha_rice" }
        assertNotNull(johaRice)
        assertEquals("Bhogali Joha Aromatic Rice", johaRice!!.nameEn)
        assertEquals("kg", johaRice.unit)
        assertEquals("Biswanath", johaRice.sellerDistrict)

        val kajiNemu = InitialData.products.find { it.id == "prod_kaji_nemu" }
        assertNotNull(kajiNemu)
        assertEquals("Sootea", kajiNemu!!.sellerVillage)
    }

    @Test
    fun testLocalizationStrings() {
        val appTitleEn = AppStrings.get("app_title", AppLanguage.ENGLISH)
        val appTitleAs = AppStrings.get("app_title", AppLanguage.ASSAMESE)
        assertEquals("Amar Haat Assam", appTitleEn)
        assertEquals("আমাৰ হাট অসম", appTitleAs)

        val taglineAs = AppStrings.get("app_tagline", AppLanguage.ASSAMESE)
        assertTrue(taglineAs.contains("ঘৰলৈ"))
    }
}
