package com.mohsen.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.mohsen.webview.ui.theme.WebviewTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WebviewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Chart WebView takes half the space
                        WebViewChart(modifier = Modifier.weight(1f))
                        // URL WebView takes the remaining space
                        WebViewUrl(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun WebViewChart(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val webView = WebView(context).apply {
        settings.javaScriptEnabled = true
        webViewClient = WebViewClient()
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

    AndroidView(factory = { webView }, modifier = modifier)
}

@Composable
fun WebViewUrl(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val webView = WebView(context).apply {
        settings.javaScriptEnabled = true
        webViewClient = WebViewClient()
        loadUrl("https://www.baexpert.ir")
    }

    AndroidView(factory = { webView }, modifier = modifier)
}
