package com.example.perludilindungi.ui.lokasivaksin

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.perludilindungi.adapter.FaskesAdapter
import com.example.perludilindungi.databinding.FragmentLokasiVaksinBinding
import com.example.perludilindungi.model.Faskes
import com.example.perludilindungi.model.ProvinceCity
import com.example.perludilindungi.utils.LocationUtils
import com.example.perludilindungi.viewmodel.ViewModelFactory
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

class LokasiVaksinFragment : Fragment() {
    private val listKota = ArrayList<String>()
    private val listProvinsi = ArrayList<String>()
    private val listFaskes = ArrayList<Faskes>()
    private val faskesAdapter = FaskesAdapter(listFaskes)

    private var selectedProvince = ""
    private var selectedCity = ""

    private lateinit var _binding : FragmentLokasiVaksinBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    // LocationCallback - Called when FusedLocationProviderClient has a new Location.
    private lateinit var locationCallback: LocationCallback

    private var currentLocation: Location? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fusedLocationClient = activity?.let { LocationServices.getFusedLocationProviderClient(it) }!!
        _binding = FragmentLokasiVaksinBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lokasiVaksinViewModel = activity?.let { obtainViewModel(it) }

        locationRequest = LocationRequest().apply {
            // Sets the desired interval for
            // active location updates.
            // This interval is inexact.
            interval = TimeUnit.SECONDS.toMillis(30)

            // Sets the fastest rate for active location updates.
            // This interval is exact, and your application will never
            // receive updates more frequently than this value
            fastestInterval = TimeUnit.SECONDS.toMillis(10)

            // Sets the maximum time when batched location
            // updates are delivered. Updates may be
            // delivered sooner than this interval
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                if (p0 !== null) {
                    currentLocation = p0.lastLocation
                } else {
                    Log.d(TAG, "Informasi lokasi tidak tersedia.")
                }
            }
        }


        if (lokasiVaksinViewModel != null) {

            lokasiVaksinViewModel.selectedProvince.observe(viewLifecycleOwner, { e ->
                selectedProvince = e
            })

            lokasiVaksinViewModel.selectedCity.observe(viewLifecycleOwner, { e ->
                selectedCity = e
            })

            lokasiVaksinViewModel.listFaskes.observe(viewLifecycleOwner, { listFaskes ->
                setFaskesData(listFaskes)
            })

            lokasiVaksinViewModel.listProvince.observe(viewLifecycleOwner, { listProvince ->
                setProvincesData(listProvince)
            })

            lokasiVaksinViewModel.listCity.observe(viewLifecycleOwner, { listCity ->
                setCitiesData(listCity)
            })

            lokasiVaksinViewModel.isLoading.observe(viewLifecycleOwner, {
                showLoading(it)
            })

            lokasiVaksinViewModel.isToast.observe(viewLifecycleOwner, { isToast ->
                showToast(isToast, lokasiVaksinViewModel.toastReason.value.toString())
            })

            setupSpinners(lokasiVaksinViewModel)

            lokasiVaksinViewModel.getProvincesData()

            binding.btnCari.setOnClickListener {
                if (selectedProvince.equals("") || selectedCity.equals("")) {
                    Toast.makeText(activity, "Silakan pilih provinsi dan kota terlebih dahulu", Toast.LENGTH_SHORT).show()
                } else {
                    lokasiVaksinViewModel.getFaskesData()
                }
            }
        }
    }

    private fun obtainViewModel(activity: FragmentActivity) : LokasiVaksinViewModel? {
        val viewModelFactory = ViewModelFactory.getInstance(activity.application)
        return viewModelFactory?.let {
            ViewModelProvider(activity,
                it
            ).get(LokasiVaksinViewModel::class.java)
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        binding.rvFaskes.layoutManager = layoutManager
        binding.rvFaskes.adapter = faskesAdapter
    }

    private fun setupSpinners(lokasiVaksinViewModel: LokasiVaksinViewModel) {
        binding.spinnerProvinsi.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedProvince = p0?.getItemAtPosition(p2).toString()
                    if (!lokasiVaksinViewModel.selectedProvince.value.isNullOrBlank()
                        && selectedProvince.isBlank()) {
                        binding.spinnerProvinsi
                            .setSelection(listProvinsi.indexOf(lokasiVaksinViewModel.selectedProvince.value))
                    } else {
                        lokasiVaksinViewModel.setSelectedProvince(selectedProvince)
                    }
                    if (selectedProvince.isNotBlank()) {
                        lokasiVaksinViewModel.getCitiesData()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        binding.spinnerKota.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedCity = p0?.getItemAtPosition(p2).toString()
                    if (!lokasiVaksinViewModel.selectedCity.value.isNullOrBlank()
                        && selectedCity.isBlank()) {
                        binding.spinnerKota
                            .setSelection(listKota.indexOf(lokasiVaksinViewModel.selectedCity.value))
                    } else {
                        lokasiVaksinViewModel.setSelectedCity(selectedCity)
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
    }

    private fun setFaskesData(data: ArrayList<Faskes>) {
        listFaskes.clear()
        if (activity?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            } !==
            PackageManager.PERMISSION_GRANTED) {
            if (activity?.let {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        it,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                }!!) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            }
        } else {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                Log.e(TAG, "${location.latitude}, ${location.longitude}")
                data.sortBy {
                    it.latitude?.let { it1 ->
                        it.longitude?.let { it2 ->
                            LocationUtils.distance(
                                location.latitude, location.longitude,
                                it1.toDouble(), it2.toDouble()
                            )
                        }
                    }
                }
                if (data.size < 5) {
                    listFaskes.addAll(data)
                } else {
                    listFaskes.addAll(data.slice(0..4))
                }
                faskesAdapter.notifyDataSetChanged()

            } else {
                Toast.makeText(activity, "Gagal mendapatkan lokasi Anda", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setProvincesData(data: List<ProvinceCity>) {
        listProvinsi.clear()
        listProvinsi.add("")
        for (provinsi in data) {
            provinsi.key?.let { listProvinsi.add(it) }
        }

        val adapter = activity?.let {
            ArrayAdapter(
                it, android.R.layout.simple_spinner_item,
                listProvinsi)
        }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerProvinsi.adapter = adapter
    }

    private fun setCitiesData(data: List<ProvinceCity>) {
        listKota.clear()
        listKota.add("")
        for (kota in data) {
            kota.key?.let { listKota.add(it) }
        }
        val adapter = activity?.let {
            ArrayAdapter(
                it, android.R.layout.simple_spinner_item,
                listKota)
        }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerKota.adapter = adapter
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(isToast: Boolean, toastReason: String) {
        if (!isToast) {
            Toast.makeText(context, toastReason, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private val TAG = LokasiVaksinFragment::class.java.simpleName
    }
}