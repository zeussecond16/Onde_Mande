package com.example.web_app_ondemande.ui.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.web_app_ondemande.ui.theme.OndeMandeTheme

/**
 * Komponen Stateless Form Data Pemesan pada Halaman Keranjang.
 *
 * Fitur Compose:
 * - OutlinedTextField dengan gaya selaras Material 3, rounded corner 16.dp, dan penanganan status isError + supportingText.
 * - KeyboardOptions & KeyboardActions untuk navigasi perpindahan fokus input (ImeAction.Next & ImeAction.Done via FocusManager).
 * - AnimatedVisibility dengan expandVertically + fadeIn untuk memunculkan/menyembunyikan kolom alamat pengantaran secara halus.
 *
 * Bagian Tampilan:
 * - Menampilkan bidang input Nama Lengkap, Nomor Telepon, serta Alamat Pengantaran (bersyarat jika opsi "Antar" dipilih).
 */
@Composable
fun CustomerFormSection(
    name: String,
    onNameChange: (String) -> Unit,
    nameError: String?,
    phone: String,
    onPhoneChange: (String) -> Unit,
    phoneError: String?,
    address: String,
    onAddressChange: (String) -> Unit,
    addressError: String?,
    isDelivery: Boolean,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Data Pemesan",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        // 1. Field Nama Lengkap
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Nama Lengkap") },
            placeholder = {
                Text(
                    text = "Contoh: Budi Santoso",
                    color = MaterialTheme.colorScheme.secondary
                )
            },
            singleLine = true,
            isError = nameError != null,
            supportingText = nameError?.let {
                { Text(text = it, color = MaterialTheme.colorScheme.error) }
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // 2. Field Nomor Telepon
        OutlinedTextField(
            value = phone,
            onValueChange = { newValue ->
                // Filter hanya menerima angka dan tanda '+' pada posisi karakter pertama
                val filtered = newValue.filterIndexed { index, c ->
                    c.isDigit() || (index == 0 && c == '+')
                }
                onPhoneChange(filtered)
            },
            label = { Text("Nomor Telepon") },
            placeholder = {
                Text(
                    text = "Contoh: 081234567890",
                    color = MaterialTheme.colorScheme.secondary
                )
            },
            singleLine = true,
            isError = phoneError != null,
            supportingText = phoneError?.let {
                { Text(text = it, color = MaterialTheme.colorScheme.error) }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = if (isDelivery) ImeAction.Next else ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
                onDone = { focusManager.clearFocus() }
            ),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // 3. Field Alamat Pengantaran (Hanya tampil jika opsi "Antar" dipilih)
        AnimatedVisibility(
            visible = isDelivery,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            OutlinedTextField(
                value = address,
                onValueChange = onAddressChange,
                label = { Text("Alamat Pengantaran") },
                placeholder = {
                    Text(
                        text = "Contoh: Jl. Sudirman No. 12, Kel. Padang Barat",
                        color = MaterialTheme.colorScheme.secondary
                    )
                },
                minLines = 2,
                maxLines = 4,
                isError = addressError != null,
                supportingText = addressError?.let {
                    { Text(text = it, color = MaterialTheme.colorScheme.error) }
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomerFormSectionEmptyPreview() {
    OndeMandeTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            CustomerFormSection(
                name = "",
                onNameChange = {},
                nameError = null,
                phone = "",
                onPhoneChange = {},
                phoneError = null,
                address = "",
                onAddressChange = {},
                addressError = null,
                isDelivery = false,
                focusManager = LocalFocusManager.current
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomerFormSectionErrorPreview() {
    OndeMandeTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            CustomerFormSection(
                name = "A",
                onNameChange = {},
                nameError = "Nama minimal 2 karakter",
                phone = "0123",
                onPhoneChange = {},
                phoneError = "Nomor telepon harus diawali 08, 62, atau +62",
                address = "",
                onAddressChange = {},
                addressError = "Alamat wajib diisi untuk layanan antar",
                isDelivery = true,
                focusManager = LocalFocusManager.current
            )
        }
    }
}
