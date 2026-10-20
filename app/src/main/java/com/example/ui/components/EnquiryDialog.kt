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
import com.example.data.local.ProductEntity
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.AssamForestGreen
import com.example.ui.theme.AssamTextPrimary
import com.example.ui.theme.AssamTextSecondary

@Composable
fun EnquiryDialog(
    product: ProductEntity,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmit: (buyerName: String, buyerPhone: String, quantity: Double, unit: String, message: String, contactMethod: String) -> Unit
) {
    var buyerName by remember { mutableStateOf("") }
    var buyerPhone by remember { mutableStateOf("") }
    var quantityText by remember { mutableStateOf(product.minOrderQuantity.toString()) }
    var message by remember { mutableStateOf("") }
    var contactMethod by remember { mutableStateOf("PHONE") }
    var hasError by remember { mutableStateOf(false) }

    val productName = if (language == AppLanguage.ASSAMESE) product.nameAs else product.nameEn

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("enquiry_dialog")
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
                        text = if (language == AppLanguage.ASSAMESE) "বিক্ৰেতালৈ অনুসন্ধান" else "Send Direct Enquiry",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AssamForestGreen
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }

                Text(
                    text = "$productName • ₹${product.price.toInt()}/${product.unit}",
                    fontSize = 13.sp,
                    color = AssamTextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${if (language == AppLanguage.ASSAMESE) "বিক্ৰেতা: " else "Seller: "}${product.sellerName} (${product.sellerVillage})",
                    fontSize = 12.sp,
                    color = AssamTextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = buyerName,
                    onValueChange = {
                        buyerName = it
                        hasError = false
                    },
                    label = { Text(if (language == AppLanguage.ASSAMESE) "আপোনাৰ সম্পূৰ্ণ নাম *" else "Your Full Name *") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("enquiry_buyer_name_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = buyerPhone,
                    onValueChange = {
                        buyerPhone = it
                        hasError = false
                    },
                    label = { Text(if (language == AppLanguage.ASSAMESE) "মোবাইল নম্বৰ (১০ টা অংক) *" else "Mobile Number (10 digits) *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("enquiry_buyer_phone_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = { quantityText = it },
                        label = { Text(if (language == AppLanguage.ASSAMESE) "পৰিমাণ *" else "Quantity *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("enquiry_quantity_input")
                    )

                    OutlinedTextField(
                        value = product.unit,
                        onValueChange = {},
                        label = { Text(if (language == AppLanguage.ASSAMESE) "একক" else "Unit") },
                        enabled = false,
                        singleLine = true,
                        modifier = Modifier.width(90.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text(if (language == AppLanguage.ASSAMESE) "বাৰ্তা বা বিশেষ অনুৰোধ (ঐচ্ছিক)" else "Message / Requirements (Optional)") },
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("enquiry_message_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (language == AppLanguage.ASSAMESE) "পছন্দৰ যোগাযোগ মাধ্যম:" else "Preferred Contact Method:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AssamTextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        RadioButton(
                            selected = contactMethod == "PHONE",
                            onClick = { contactMethod = "PHONE" }
                        )
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "ফোন" else "Phone",
                            fontSize = 13.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        RadioButton(
                            selected = contactMethod == "WHATSAPP",
                            onClick = { contactMethod = "WHATSAPP" }
                        )
                        Text(
                            text = if (language == AppLanguage.ASSAMESE) "হোৱাটছএপ" else "WhatsApp",
                            fontSize = 13.sp
                        )
                    }
                }

                if (hasError) {
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "অনুগ্ৰহ কৰি নাম আৰু ফোন নম্বৰ লিখক" else "Please enter your name and valid phone number",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val qty = quantityText.toDoubleOrNull() ?: product.minOrderQuantity
                        if (buyerName.isBlank() || buyerPhone.length < 10) {
                            hasError = true
                        } else {
                            onSubmit(
                                buyerName.trim(),
                                buyerPhone.trim(),
                                qty,
                                product.unit,
                                message.trim(),
                                contactMethod
                            )
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AssamForestGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_enquiry_button")
                ) {
                    Text(
                        text = if (language == AppLanguage.ASSAMESE) "অনুসন্ধান পঠিয়াওক" else "Submit Enquiry",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
