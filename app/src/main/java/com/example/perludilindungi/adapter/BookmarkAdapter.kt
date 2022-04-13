package com.example.perludilindungi.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.perludilindungi.R
import com.example.perludilindungi.database.BookmarkDB
import com.example.perludilindungi.databinding.FaskesItemBinding
import com.example.perludilindungi.ui.lokasivaksin.FaskesDetailActivity

class BookmarkAdapter(val listFaskes: ArrayList<BookmarkDB>) : RecyclerView.Adapter<BookmarkAdapter.FaskesViewHolder>() {

    constructor() : this(ArrayList<BookmarkDB>())

    class FaskesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = FaskesItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaskesViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.faskes_item, parent,false)
        return FaskesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaskesViewHolder, position: Int) {
        with(holder) {
            val faskes = listFaskes[position]
            binding.tvNamaFaskes.text = faskes.nama
            binding.tvAlamatFaskes.text = faskes.alamat
            binding.tvTeleponFaskes.text = faskes.telp
            binding.tvKodeFaskes.text = "KODE: " + faskes.kode

            binding.tvJenisFaskes.text = faskes.jenisFaskes
            if (faskes.jenisFaskes.equals("RUMAH SAKIT")) {
                binding.tlJenisFaskes
                    .setBackgroundColor(Color.parseColor("#FF69B4"))
            } else if (faskes.jenisFaskes.equals("KLINIK")) {
                binding.tlJenisFaskes
                    .setBackgroundColor(Color.parseColor("#FF018786"))
            } else if (faskes.jenisFaskes.equals("PUSKESMAS")) {
                binding.tlJenisFaskes
                    .setBackgroundColor(Color.parseColor("#5b60ee"))
            } else if (faskes.jenisFaskes.isNullOrBlank()) {
                binding.tlJenisFaskes
                    .setBackgroundColor(Color.GRAY)
                binding.tvJenisFaskes.text = "TIDAK ADA DATA"
            }

            holder.itemView.setOnClickListener {
                val faskesData = listFaskes[position]
                val activity = it!!.context as AppCompatActivity
                val intent = Intent(activity, FaskesDetailActivity::class.java)
                intent.putExtra("Bookmark", faskesData)
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return listFaskes.size
    }
}