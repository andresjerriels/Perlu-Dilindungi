package com.example.perludilindungi.ui.news

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.perludilindungi.R
import com.example.perludilindungi.model.News
import coil.load
import com.example.perludilindungi.databinding.NewsItemBinding


class Adapter(private val newsList: ArrayList<News>): RecyclerView.Adapter<Adapter.Holder>() {
    constructor() : this(ArrayList<News>())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(holder) {
            val news = newsList[position]
            binding.newsTitle.text = news.title
            binding.newsDate.text = news.pubDate
            bindImage(binding.newsImage, news.enclosure?._url)

            // space for webView intent
            holder.itemView.setOnClickListener {
                newsList[position].enclosure?._length = newsList[position].guid
                val news = newsList[position].enclosure
                val activity = it!!.context as AppCompatActivity
                val intent = Intent (activity, NewsWebViewActivity::class.java)
                intent.putExtra("News", news)
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = newsList.size

    fun setData(data: ArrayList<News>) {
        newsList.clear()
        newsList.addAll(data)
    }

    class Holder(view: View): RecyclerView.ViewHolder(view) {
        val binding = NewsItemBinding.bind(view)
    }

    fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri) {
                placeholder(R.drawable.loading)
                error(R.drawable.broken_image)
            }
        }
    }
}