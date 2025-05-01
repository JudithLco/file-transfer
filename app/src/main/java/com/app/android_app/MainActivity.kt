package com.app.android_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.android_app.ui.theme.*
import com.app.android_app.ui.upload.UploadScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidappTheme {
                val nav = rememberNavController()
                AppNavigation(nav)
            }
        }
    }
}

@Composable
fun AppNavigation(nav: NavHostController){
    NavHost(nav, startDestination = "main"){
        composable("main") {
            MainScreen(nav)
        }
        composable("upload") {
            UploadScreen()
        }
        composable("download") {
            DownloadScreen()
        }
        composable("list"){
            ListScreen()
        }
    }
}

@Composable
fun ListScreen() {
    TODO("Not yet implemented")
}

@Composable
fun DownloadScreen() {
    TODO("Not yet implemented")
}

@Composable
fun MainScreen(nav: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { nav.navigate("upload") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send a file")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { nav.navigate("download") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Receive a file")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { nav.navigate("list") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("List all files")
        }
    }
}
