package com.example.wikiapp.view

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.wikiapp.R
import com.example.wikiapp.databinding.ActivityWebViewBinding
import com.example.wikiapp.utils.CacheInterceptor
import com.example.wikiapp.utils.NetworkUtils
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.IOException


class WebViewActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityWebViewBinding

    lateinit var wb: WebView
    lateinit var okHttpClient: OkHttpClient
    lateinit var cache: Cache
    var progressBar: ProgressBar? = null
    var toolbar: androidx.appcompat.widget.Toolbar? = null
    var webview: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_web_view)

        var url = intent.extras?.get("url").toString()
        var weburl = findViewById<TextView>(R.id.webURL)
        weburl.text = url

        var progressBar = findViewById<ProgressBar>(R.id.progressBar);
        var webview = findViewById<WebView>(R.id.webView);
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            Toast.makeText(this, "Loading Online", Toast.LENGTH_SHORT).show()

        } else {
//            webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            Toast.makeText(this, "Loading Offline", Toast.LENGTH_SHORT).show()

        }

        val httpCacheDirectory = File(applicationContext.cacheDir, "http-cache")
        val cacheSize = 30 * 1024 * 1024 // 30 MiB
        val cache = Cache(httpCacheDirectory, cacheSize.toLong())
        okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(CacheInterceptor())
            .cache(cache)
            .build()
        wb = findViewById(R.id.webView)
        wb.webViewClient = object : WebViewClient() {
            //  API 21-
            @SuppressWarnings("deprecation")
            override fun shouldInterceptRequest(
                view: WebView?,
                url: String
            ): WebResourceResponse? {
                return this.getNewResponse(url)
            }

            //  API 21+
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest
            ): WebResourceResponse? {
                return getNewResponse(request.url.toString())
            }

            private fun getNewResponse(url: String): WebResourceResponse? {
                return try {
                    val okHttpRequest: Request = Request.Builder().url(url).build()
                    val response: Response = okHttpClient.newCall(okHttpRequest).execute()
                    WebResourceResponse("", "", response.body!!.byteStream())
                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }
            }
        }
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);

        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);

        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.setScrollbarFadingEnabled(false);
        wb.loadUrl(url)

    }


}
