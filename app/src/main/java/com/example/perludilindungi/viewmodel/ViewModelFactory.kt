package com.example.perludilindungi.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.perludilindungi.database.BookmarkRoom
import com.example.perludilindungi.ui.bookmark.BookmarkViewModel
import com.example.perludilindungi.ui.lokasivaksin.LokasiVaksinViewModel

class ViewModelFactory() : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LokasiVaksinViewModel::class.java)) {
            return LokasiVaksinViewModel() as T
        } else if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)) {
            return BookmarkViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

        companion object {
            @Volatile
            private var INSTANCE: ViewModelFactory? = null

            @JvmStatic
            fun getInstance(context: Context): ViewModelFactory? {
                if (INSTANCE == null) {
                    synchronized(ViewModelFactory::class.java) {
                        if (INSTANCE == null) {
                            INSTANCE = ViewModelFactory()
                        }
                    }

                }
                return INSTANCE
            }
        }
}