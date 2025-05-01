package com.app.android_app.ui.upload

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.File
import java.io.FileOutputStream
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign

@Composable
fun UploadScreen(viewModel: UploadViewModel = viewModel()) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    var fileName by remember { mutableStateOf<String?>(null) }
    var fileToUpload by remember { mutableStateOf<File?>(null) }
    var deleteAfterUse by remember { mutableStateOf(false) }
    var expirationHours by remember { mutableStateOf("1") }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    var showCopiedMessage by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val file = uriToFile(it, context)
            fileName = file.name
            fileToUpload = file
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Upload",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { launcher.launch("*/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Chose a file")
            }

            Spacer(Modifier.height(8.dp))

            fileName?.let {
                Text(
                    text = "Selected file: $it",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = deleteAfterUse,
                    onCheckedChange = { deleteAfterUse = it }
                )
                Text("Delete after use")
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "File Expiration Time:",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Set how many hours from now the file will expire. Between 1-168h.",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = expirationHours,
                onValueChange = { expirationHours = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Hours (1-168)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                )
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    fileToUpload?.let {
                        viewModel.uploadFile(
                            file = it,
                            deleteAfterUse = deleteAfterUse,
                            expiration = expirationHours.toLongOrNull()?.times(3600) ?: 3600
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = fileToUpload != null
            ) {
                Text("Upload")
            }

            Spacer(Modifier.height(24.dp))

            when (val s = state) {
                is UploadState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                is UploadState.Success -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Upload Successful",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF4CAF50)
                        )

                        Spacer(Modifier.height(8.dp))

                        Text("File: ${s.response.name}")

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "ID: ${s.response.id}",
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(s.response.id))
                                    showCopiedMessage = true
                                }
                            ) {
                                Text("Copy")
                            }
                        }

                        if (showCopiedMessage) {
                            Text(
                                "Copied to clipboard",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Text("Expires: ${s.response.expirationTime}")
                        Text("Delete after use: ${if (s.response.deleteAfterUse) "Yes" else "No"}")
                    }
                }
                is UploadState.Error -> Text(
                    "Error: ${s.message}",
                    color = MaterialTheme.colorScheme.error
                )
                UploadState.Idle -> {}
            }
        }
    }
}
fun uriToFile(uri: Uri, context: Context): File {
    val fileName = context.contentResolver.query(uri, null, null, null, null)
        ?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        } ?: "temp.upload"

    val file = File(context.cacheDir, fileName)
    context.contentResolver.openInputStream(uri)?.use { input ->
        FileOutputStream(file).use { output -> input.copyTo(output) }
    }
    return file
}
