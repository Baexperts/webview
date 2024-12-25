package com.mohsen.webview

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import com.mohsen.webview.ui.theme.WebviewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WebviewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WebViewChart(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun WebViewChart(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // WebView setup
    val webView = WebView(context).apply {
        settings.javaScriptEnabled = true
        webViewClient = WebViewClient() // Handle redirects within the WebView
        loadDataWithBaseURL(
            null,
            """
            <html>
                <head>
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                </head>
                <body>
                    <canvas id="myChart" width="400" height="400"></canvas>
                    <script>
                        var ctx = document.getElementById('myChart').getContext('2d');
                        var myChart = new Chart(ctx, {
                            type: 'line',
                            data: {
                                labels: ['January', 'February', 'March', 'April', 'May'],
                                datasets: [{
                                    label: 'Demo Chart',
                                    data: [12, 19, 3, 5, 2],
                                    borderColor: 'rgba(255, 99, 132, 1)',
                                    borderWidth: 1
                                }]
                            },
                            options: {
                                scales: {
                                    y: {
                                        beginAtZero: true
                                    }
                                }
                            }
                        });
                    </script>
                </body>
            </html>
            """.trimIndent(),
            "text/html", "utf-8", null
        )
    }

    // Place the WebView in the Compose layout
    androidx.compose.ui.viewinterop.AndroidView(
        factory = { webView },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WebviewTheme {
        WebViewChart()
    }
}
