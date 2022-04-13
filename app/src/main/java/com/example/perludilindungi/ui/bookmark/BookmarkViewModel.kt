package com.example.perludilindungi.ui.bookmark

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.perludilindungi.database.BookmarkDB
import com.example.perludilindungi.database.BookmarkRoom
import com.example.perludilindungi.model.Faskes

class BookmarkViewModel : ViewModel() {

    private lateinit var bookmarkList: LiveData<List<BookmarkDB>>

    fun getBookmarkList(context: Context): LiveData<List<BookmarkDB>> {
        val database = BookmarkRoom.getDatabase(context.applicationContext).bookmarkDAO()

        bookmarkList = database.getAllBookmarks().map { data ->
            data.map {
                    BookmarkDB(it.id, it.kode, it.nama, it.kota, it.provinsi, it.alamat, it.latitude,
                    it.longitude, it.telp, it.jenisFaskes, it.kelasRs, it.status, it.sourceData)
            }
        }

        return bookmarkList
    }
}