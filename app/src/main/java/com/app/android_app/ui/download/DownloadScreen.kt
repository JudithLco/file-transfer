// DownloadScreen.kt
package com.app.android_app.ui.download

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.File

@Composable
fun DownloadScreen(
    viewModel: DownloadViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    var fileId by remember { mutableStateOf("") }
    var saveLocally by remember { mutableStateOf(true) }

    val fileSaver = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(),
        onResult = { uri ->
            uri?.let {
                when (val currentState = state) {
                    is DownloadState.Success -> {
                        context.contentResolver.openOutputStream(uri)?.use { output ->
                            output.write(currentState.data)
                        }
                    }
                    else -> {}
                }
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Download",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = fileId,
            onValueChange = { fileId = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("File ID") },
            placeholder = { Text("Enter file ID (xxxx-xxxx-xxxx)") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = saveLocally,
                onCheckedChange = { saveLocally = it }
            )
            Text("Save to device")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (fileId.isNotBlank()) {
                    viewModel.downloadFile(fileId)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = fileId.isNotBlank()
        ) {
            Text("Download")
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (val currentState = state) {
            is DownloadState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is DownloadState.Success -> {
                Column {
                    Text(
                        "Download successful!",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text("File: ${currentState.file.name}")

                    if (saveLocally) {
                        LaunchedEffect(currentState) {
                            fileSaver.launch(currentState.file.name)
                        }
                    }
                }
            }
            is DownloadState.Error -> {
                Text(
                    "Error: ${currentState.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
            DownloadState.Idle -> {}
        }
    }
}