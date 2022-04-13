package com.example.perludilindungi.ui.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.perludilindungi.R
import com.example.perludilindungi.api.ApiConfig
import com.example.perludilindungi.databinding.FragmentNewsBinding
import com.example.perludilindungi.model.News
import com.example.perludilindungi.model.NewsResponse
import kotlinx.android.synthetic.main.fragment_news.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

class NewsFragment : Fragment() {
  // view model
  private val newsViewModel: NewsViewModel by viewModels()

  private val listNews = ArrayList<News>()
  private val newsAdapter = Adapter(listNews)

  private lateinit var _binding : FragmentNewsBinding
  private val binding get() = _binding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentNewsBinding.inflate(inflater, container, false)
    setupRecyclerView()
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    

    binding.progressBar.visibility = View.VISIBLE
    val client = ApiConfig.getApiService().getNews()

    client.enqueue(object : retrofit2.Callback<NewsResponse>{
      override fun onResponse(
        call: retrofit2.Call<NewsResponse>,
        response: retrofit2.Response<NewsResponse>
      ) {
        if (response.isSuccessful) {
          val responseBody = response.body()
          if (responseBody != null) {
            Log.v("heyo", responseBody.toString())
            listNews.clear()
            binding.progressBar.visibility = View.GONE
            if (responseBody.results.isNotEmpty()) {
              binding.progressBar.visibility = View.GONE
              setNews(responseBody.results)
            }
            else {
              Log.v("heyo", "data is empty")
            }
          }
        } else {
          Log.e(TAG, "onFailure1: ${response.message()}")
        }
      }

      override fun onFailure(call: retrofit2.Call<NewsResponse>, t:Throwable) {
        Log.e(TAG, "onFailure2: ${t.message}")
      }
    })
  }

  private fun setNews(data: List<News>) {
    listNews.addAll(data)
    Log.v("heyo", "74")
    newsAdapter.notifyDataSetChanged()
  }

  private fun setupRecyclerView() {
    val layoutManager = LinearLayoutManager(context)
    binding.newsRecyclerView.layoutManager = layoutManager
    binding.newsRecyclerView.adapter = newsAdapter
  }

  companion object {
    private val TAG = NewsFragment::class.java.simpleName
  }

//  Disimpen aja siapa tau kepake lagi
//  private val newsViewModel: NewsViewModel by viewModels()
//  private var _binding: FragmentNewsBinding? = null
//
//  val list = ArrayList<NewsData>()
//  val listTitle = arrayOf(
//    "Judul 1 lorem ipsum sit dolor amet",
//    "Judul 2 lorem ipsum sit dolor amet",
//    "Judul 3 lorem ipsum sit dolor amet",
//    "Judul 4 lorem ipsum sit dolor amet",
//    "Judul 5 lorem ipsum sit dolor amet",
//    "Judul 6 lorem ipsum sit dolor amet",
//    "Judul 7 lorem ipsum sit dolor amet",
//    "Judul 8 lorem ipsum sit dolor amet",
//    "Judul 9 lorem ipsum sit dolor amet",
//    "Judul 10 lorem ipsum sit dolor amet",)
//
//  // This property is only valid between onCreateView and
//  // onDestroyView.
//  private val binding get() = _binding!!
//
//  override fun onCreateView(
//    inflater: LayoutInflater,
//    container: ViewGroup?,
//    savedInstanceState: Bundle?
//  ): View? {
////    NewsViewModel =
////            ViewModelProvider(this).get(NewsViewModel::class.java)
//
//    _binding = FragmentNewsBinding.inflate(inflater, container, false)
//    val root: View = binding.root
//
////    val textView: TextView = binding.textNews
////    newsViewModel.text.observe(viewLifecycleOwner, Observer {
////      textView.text = it
////    })
//
//    // recycler view
//    binding.newsRecyclerView.setHasFixedSize(true)
//    binding.newsRecyclerView.layoutManager = LinearLayoutManager(this.context)
//
//    list.clear()
//    for (i in 0 until listTitle.size) {
//      list.add(NewsData(i,listTitle.get(i),"22 Februari 2022"))
//    }
//    // init the adapter that we created before
//    val adapter = Adapter(list)
//    adapter.notifyDataSetChanged()
//
//    // show the data in recycle view
//    binding.newsRecyclerView.adapter = adapter
//
//    return root
//  }
//
//override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}