package com.example.perludilindungi.ui.lokasivaksin

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.perludilindungi.R
import com.example.perludilindungi.database.BookmarkDB
import com.example.perludilindungi.database.BookmarkRoom
import com.example.perludilindungi.databinding.ActivityFaskesDetailBinding
import com.example.perludilindungi.model.Faskes
import kotlinx.android.synthetic.main.activity_faskes_detail.*

class FaskesDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFaskesDetailBinding
    private var isBookmarked : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faskes_detail)

        binding = ActivityFaskesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("Faskes")) {
            val faskes = intent.getParcelableExtra<Faskes>("Faskes")
            if (faskes != null) {
                isBookmarked = faskes.kode?.let { checkIfFaskesBookmarked(it) }!!
                setupFaskesUi(faskes)
            }
        } else if (intent.hasExtra("Bookmark")) {
            val bookmark = intent.getParcelableExtra<BookmarkDB>("Bookmark")
            if (bookmark != null) {
                isBookmarked = bookmark.kode?.let { checkIfFaskesBookmarked(it) }!!
                setupBookmarkUi(bookmark)
            }
        }

        val actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "Detail Faskes"
        }
    }

    private fun setupFaskesUi(faskes: Faskes) {
        showData(faskes)

        binding.btnBookmark.setOnClickListener {
            val database =
                BookmarkRoom.getDatabase(applicationContext).bookmarkDAO()

            if (isBookmarked) {
                faskes.kode?.let { it1 -> database.delete(it1) }

                Toast.makeText(applicationContext,
                    "${faskes.nama} berhasil dihapus dari bookmark",
                    Toast.LENGTH_LONG).show()

                isBookmarked = false
                switchBookmarkButton()
            } else {
                val bookmark = faskes.id?.let { it1 -> BookmarkDB(it1,
                    faskes.kode, faskes.nama, faskes.kota, faskes.provinsi, faskes.alamat,
                    faskes.latitude, faskes.longitude, faskes.telp, faskes.jenisFaskes, faskes.kelasRs,
                    faskes.status, faskes.sourceData) }

                if (bookmark != null) {
                    database.insert(bookmark)
                    Toast.makeText(applicationContext,
                        "${faskes.nama} berhasil ditambahkan ke dalam bookmark",
                        Toast.LENGTH_LONG).show()

                    isBookmarked = true
                    switchBookmarkButton()
                }
            }

        }
    }

    private fun setupBookmarkUi(faskes: BookmarkDB) {
        showData(faskes)

        binding.btnBookmark.setOnClickListener {
            val database =
                BookmarkRoom.getDatabase(applicationContext).bookmarkDAO()

            if (isBookmarked) {
                faskes.kode?.let { it1 -> database.delete(it1) }
                Toast.makeText(applicationContext,
                    "${faskes.nama} berhasil dihapus dari bookmark",
                    Toast.LENGTH_LONG).show()

                isBookmarked = false
                switchBookmarkButton()

            } else {
                val bookmark = faskes.id.let { it1 -> BookmarkDB(it1,
                    faskes.kode, faskes.nama, faskes.kota, faskes.provinsi, faskes.alamat,
                    faskes.latitude, faskes.longitude, faskes.telp, faskes.jenisFaskes, faskes.kelasRs, faskes.status,
                    faskes.sourceData) }

                database.insert(bookmark)
                Toast.makeText(applicationContext,
                    "${faskes.nama} berhasil ditambahkan ke dalam bookmark",
                    Toast.LENGTH_LONG).show()

                isBookmarked = true
                switchBookmarkButton()
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showData(faskes: Faskes) {
        binding.tvNamaFaskes.text = faskes.nama
        binding.tvKodeFaskes.text = "KODE: " + faskes.kode
        binding.tvAlamatFaskes.text = faskes.alamat
        binding.tvTeleponFaskes.text = "No. Telp: " + faskes.telp

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

        if (faskes.status.equals("Siap Vaksinasi")) {
            binding.ivStatus.setImageResource(R.drawable.ic_check_circle_green_24)
        } else {
            binding.ivStatus.setImageResource(R.drawable.ic_cross_red_24)
        }
        binding.tvStatus.text = "Fasilitas ini\n${faskes.status}"

        if (isBookmarked) {
            switchBookmarkButton()
        }

        btn_maps.setOnClickListener {
            val stringURL = String.format(
                "http://maps.google.com/maps?q=loc:%.8f,%.8f?z=10",
                faskes.latitude?.toDouble(),
                faskes.longitude?.toDouble()
            )
            val gmmIntentUri : Uri = Uri.parse(stringURL)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

    private fun showData(faskes: BookmarkDB) {
        binding.tvNamaFaskes.text = faskes.nama
        binding.tvKodeFaskes.text = "KODE: " + faskes.kode
        binding.tvAlamatFaskes.text = faskes.alamat
        binding.tvTeleponFaskes.text = "No. Telp: " + faskes.telp

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

        if (faskes.status.equals("Siap Vaksinasi")) {
            binding.ivStatus.setImageResource(R.drawable.ic_check_circle_green_24)
        } else {
            binding.ivStatus.setImageResource(R.drawable.ic_cross_red_24)
        }
        binding.tvStatus.text = "Fasilitas ini\n${faskes.status}"

        if (isBookmarked) {
            switchBookmarkButton()
        }
        
        btn_maps.setOnClickListener {
            val stringURL = String.format(
                "http://maps.google.com/maps?q=loc:%.8f,%.8f?z=10",
                faskes.latitude?.toDouble(),
                faskes.longitude?.toDouble()
            )
            val gmmIntentUri : Uri = Uri.parse(stringURL)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

    private fun checkIfFaskesBookmarked(kode: String): Boolean {
        val database = BookmarkRoom.getDatabase(applicationContext).bookmarkDAO()
        return database.checkIfFaskesBookmarked(kode)
    }

    private fun switchBookmarkButton() {
        if (isBookmarked) {
            binding.btnBookmark.setBackgroundColor(Color.RED)
            binding.btnBookmark.text = getString(R.string.delete_bookmark)
        } else {
            binding.btnBookmark.setBackgroundColor(resources.getColor(R.color.pink))
            binding.btnBookmark.text = getString(R.string.bookmark)
        }
    }
}