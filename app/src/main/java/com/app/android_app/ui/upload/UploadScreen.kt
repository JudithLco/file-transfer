package com.app.android_app.ui.upload

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.File
import java.io.FileOutputStream

@Composable
fun UploadScreen(viewModel: UploadViewModel = viewModel()) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    var fileName by remember { mutableStateOf<String?>(null) }
    var fileToUpload by remember { mutableStateOf<File?>(null) }

    var deleteAfterUse by remember { mutableStateOf(false) }
    var expiration by remember { mutableStateOf(3600L) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val file = uriToFile(it, context)
            fileName = file.name
            fileToUpload = file
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { launcher.launch("*/*") }) {
            Text("Choisir un fichier")
        }

        Spacer(Modifier.height(8.dp))

        fileName?.let {
            Text("Fichier sélectionné : $it")
        }

        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(checked = deleteAfterUse, onCheckedChange = { deleteAfterUse = it })
            Text("Supprimer après usage")
        }

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = expiration.toString(),
            onValueChange = { value ->
                expiration = value.toLongOrNull() ?: 3600
            },
            label = { Text("Expiration (en secondes)") }
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                fileToUpload?.let {
                    viewModel.uploadFile(it, deleteAfterUse, expiration)
                }
            },
            enabled = fileToUpload != null
        ) {
            Text("Uploader")
        }

        Spacer(Modifier.height(16.dp))

        when (val s = state) {
            is UploadState.Loading -> CircularProgressIndicator()
            is UploadState.Success -> Text("Upload réussi : ${s.response.name}")
            is UploadState.Error -> Text("Erreur : ${s.message}")
            UploadState.Idle -> {}
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
