package com.mohsen.webview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.platform.LocalContext
import com.mohsen.webview.ui.theme.WebviewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WebviewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    WebViewChart() // Call your WebViewChart composable here
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun WebViewChart() {
    val webView = WebView(LocalContext.current).apply {
        settings.javaScriptEnabled = true
        webViewClient = WebViewClient()  // Handle redirects within the WebView
        loadDataWithBaseURL(
            null,
            "<html><head><script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script></head><body><canvas id=\"myChart\" width=\"400\" height=\"400\"></canvas><script>var ctx = document.getElementById('myChart').getContext('2d'); var myChart = new Chart(ctx, { type: 'line', data: { labels: ['January', 'February', 'March', 'April', 'May'], datasets: [{ label: 'Demo Chart', data: [12, 19, 3, 5, 2], borderColor: 'rgba(255, 99, 132, 1)', borderWidth: 1 }] }, options: { scales: { y: { beginAtZero: true } } }});</script></body></html>",
            "text/html", "utf-8", null
        )
    }
    // You can customize the WebView to update the chart dynamically here
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WebviewTheme {
        Greeting("Android")
    }
}
