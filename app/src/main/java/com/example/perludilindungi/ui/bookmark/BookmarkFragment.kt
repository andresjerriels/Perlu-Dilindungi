package com.example.perludilindungi.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.perludilindungi.adapter.BookmarkAdapter
import com.example.perludilindungi.database.BookmarkDB
import com.example.perludilindungi.databinding.FragmentBookmarkBinding
import com.example.perludilindungi.viewmodel.ViewModelFactory

class BookmarkFragment : Fragment() {

    private lateinit var _binding : FragmentBookmarkBinding
    private val binding get() = _binding

    private val listBookmark = ArrayList<BookmarkDB>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            val bookmarkViewModel = obtainViewModel(activity!!)
            bookmarkViewModel?.getBookmarkList(activity!!.applicationContext)?.observe(activity!!, { list ->
                if (list.isEmpty()) {
                    listBookmark.clear()
                    binding.rvFaskes.adapter = null
                } else {
                    setBookmarkData(list)
                }
            })
            setupRecyclerView()
        }
    }

    private fun obtainViewModel(activity: FragmentActivity) : BookmarkViewModel? {
        val viewModelFactory = ViewModelFactory.getInstance(activity.application)
        return viewModelFactory?.let {
            ViewModelProvider(activity,
                it
            ).get(BookmarkViewModel::class.java)
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        binding.rvFaskes.layoutManager = layoutManager
        binding.rvFaskes.adapter = BookmarkAdapter(listBookmark)
    }

    private fun setBookmarkData(list: List<BookmarkDB>) {
        listBookmark.clear()
        listBookmark.addAll(list)
        binding.rvFaskes.adapter = BookmarkAdapter(listBookmark)
    }
}