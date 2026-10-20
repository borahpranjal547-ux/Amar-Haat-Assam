package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.DistrictEntity
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.AssamForestGreen
import com.example.ui.theme.AssamTextPrimary
import com.example.ui.theme.AssamTextSecondary

@Composable
fun SellerRegistrationDialog(
    districts: List<DistrictEntity>,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmit: (
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
    ) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var sellerType by remember { mutableStateOf("FARMER") }
    var village by remember { mutableStateOf("") }
    var selectedDistrict by remember { mutableStateOf("Biswanath") }
    var mobile by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var descEn by remember { mutableStateOf("") }
    var descAs by remember { mutableStateOf("") }
    var consentAgreed by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("seller_registration_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "কৃষক / বিক্ৰেতা পঞ্জীয়ন" else "Farmer & Seller Registration",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = AssamForestGreen
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Text(
                    text = if (language == AppLanguage.ASSAMESE)
                        "বিনামূলীয়াকৈ আপোনাৰ ফাৰ্ম বা আত্মসহায়ক গোট পঞ্জীয়ন কৰক।"
                    else
                        "Free basic listing for rural producers across Assam.",
                    fontSize = 11.sp,
                    color = AssamTextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text(if (language == AppLanguage.ASSAMESE) "সম্পূৰ্ণ নাম *" else "Full Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("reg_full_name")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = businessName,
                    onValueChange = { businessName = it },
                    label = { Text(if (language == AppLanguage.ASSAMESE) "ফাৰ্ম বা গোটৰ নাম (ঐচ্ছিক)" else "Farm or Collective Name (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Seller Type selector
                var expandedType by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { expandedType = true },
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Text(
                            text = "${if (language == AppLanguage.ASSAMESE) "প্ৰকাৰ: " else "Type: "}$sellerType"
                        )
                    }
                    DropdownMenu(
                        expanded = expandedType,
                        onDismissRequest = { expandedType = false }
                    ) {
                        listOf(
                            "FARMER" to "ব্যক্তিগত কৃষক (Individual Farmer)",
                            "SHG" to "আত্মসহায়ক গোট (Self-Help Group)",
                            "FPO" to "কৃষক সংগঠন (FPO)",
                            "ARTISAN" to "শিল্পী / কাৰিকৰ (Artisan)",
                            "LOCAL_MANUFACTURER" to "স্থানীয় উৎপাদক (Local Maker)"
                        ).forEach { (code, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    sellerType = code
                                    expandedType = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = village,
                        onValueChange = { village = it },
                        label = { Text(if (language == AppLanguage.ASSAMESE) "গাঁও / অঞ্চল *" else "Village / Area *") },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("reg_village")
                    )

                    var expandedDist by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.width(130.dp)) {
                        OutlinedButton(
                            onClick = { expandedDist = true },
                            modifier = Modifier.fillMaxWidth().height(56.dp)
                        ) {
                            Text(selectedDistrict, fontSize = 12.sp)
                        }
                        DropdownMenu(
                            expanded = expandedDist,
                            onDismissRequest = { expandedDist = false }
                        ) {
                            districts.forEach { dist ->
                                DropdownMenuItem(
                                    text = { Text(dist.nameEn) },
                                    onClick = {
                                        selectedDistrict = dist.nameEn
                                        expandedDist = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = mobile,
                    onValueChange = { mobile = it },
                    label = { Text(if (language == AppLanguage.ASSAMESE) "মোবাইল নম্বৰ (১০ টা অংক) *" else "Phone Number (10 digits) *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("reg_mobile")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = whatsapp,
                    onValueChange = { whatsapp = it },
                    label = { Text(if (language == AppLanguage.ASSAMESE) "হোৱাটছএপ নম্বৰ (ঐচ্ছিক)" else "WhatsApp Number (Optional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = descEn,
                    onValueChange = { descEn = it },
                    label = { Text(if (language == AppLanguage.ASSAMESE) "খেতি বা সামগ্ৰীৰ চমু বিৱৰণ" else "Short description of products offered") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = consentAgreed,
                        onCheckedChange = { consentAgreed = it }
                    )
                    Text(
                        text = if (language == AppLanguage.ASSAMESE)
                            "মই ক্ৰেতাসকলৰ সৈতে যোগাযোগৰ বাবে নম্বৰ প্ৰকাশ কৰাত সন্মতি দিছো।"
                        else
                            "I consent to publish contact details for direct buyer enquiries.",
                        fontSize = 11.sp,
                        color = AssamTextSecondary,
                        lineHeight = 15.sp
                    )
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (fullName.isBlank()) {
                            errorMessage = "Please enter your full name."
                        } else if (village.isBlank()) {
                            errorMessage = "Please enter your village/town."
                        } else if (mobile.length < 10) {
                            errorMessage = "Please enter a valid 10-digit mobile number."
                        } else if (!consentAgreed) {
                            errorMessage = "Please agree to the contact consent."
                        } else {
                            onSubmit(
                                fullName.trim(),
                                businessName.trim(),
                                sellerType,
                                village.trim(),
                                selectedDistrict,
                                mobile.trim(),
                                whatsapp.trim(),
                                email.trim(),
                                descEn.trim(),
                                descAs.trim()
                            )
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AssamForestGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("reg_submit_btn")
                ) {
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "পঞ্জীয়ন নিশ্চিত কৰক" else "Submit Registration",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
