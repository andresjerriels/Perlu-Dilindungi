package com.example.perludilindungi.ui.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.perludilindungi.R
import com.example.perludilindungi.databinding.ActivityNewsWebViewBinding
import com.example.perludilindungi.model.News
import com.example.perludilindungi.model.NewsImage

class NewsWebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_web_view)

        val actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "News"
        }

        binding = ActivityNewsWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val news = intent.getParcelableExtra<NewsImage>("News")

        // get news link
        val link = news?._length

        val webView = binding.webNewsView

        webView.settings.loadsImagesAutomatically
        webView.settings.javaScriptEnabled = true

        // zooming tools
//        webView.settings.setSupportZoom(true)
//        webView.settings.builtInZoomControls
//        webView.settings.displayZoomControls

        webView.webViewClient = WebViewClient()

        // loading the url
        webView.loadUrl(link!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}