package com.hello.ktor

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hello.ktor.ui.theme.HelloKtorTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloKtorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "KTOR !",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    var showDialog  = remember { mutableStateOf(false) }
    var responseText = remember { mutableStateOf("") }
    var isLoading = remember { mutableStateOf(false) }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Hello $name!",
                modifier = modifier
            )
            Spacer(modifier = Modifier.height(50.dp))
            Button(onClick = {
                Log.d("TAG", "CALL NETWORK CALL HERE..")
                coroutineScope.launch {

                    isLoading.value = true
                    val postJson = """
                        {
                            "title": "HOO",
                            "body": "bar",
                            "userId": 1
                        }
                    """.trimIndent()

                    try {
                        val loginResponse = Network().getLogin_json(postJson)
                        Log.d("TAG", "Greeting: loginResponse = "+loginResponse)
                        responseText.value = loginResponse
                        showDialog.value = true
                    } catch (e: Exception) {
                        responseText.value = "Network error: ${e.message}"
                        showDialog.value = true
                    }
                    finally {
                        isLoading.value = false
                    }
                }
            }) {
                Text("Make POST Network Call")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                Log.d("TAG", "CALL NETWORK CALL HERE..")
                coroutineScope.launch {

                    isLoading.value = true

                    try {
                        val loginResponse = Network().Demo_GetApi()
                        Log.d("TAG", "Greeting: loginResponse = "+loginResponse)
                        responseText.value = loginResponse
                        showDialog.value = true
                    } catch (e: Exception) {
                        responseText.value = "Network error: ${e.message}"
                        showDialog.value = true
                    }
                    finally {
                        isLoading.value = false
                    }
                }
            }) {
                Text("Make GET Network Call")
            }
        }
    }

    if (isLoading.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    val scrollState = rememberScrollState()

    // Show dialog if needed
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Response") },
            text = {
                Column(modifier = Modifier.verticalScroll(scrollState)) {
                    Text(responseText.value)
                }
            },
            confirmButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("OK")
                }
            }
        )
    }

}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HelloKtorTheme {
        Greeting("KTOR")
    }
}