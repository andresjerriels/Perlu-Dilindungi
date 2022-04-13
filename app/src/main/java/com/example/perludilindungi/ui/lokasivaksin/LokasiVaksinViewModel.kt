package com.example.perludilindungi.ui.lokasivaksin

import android.location.LocationManager
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.perludilindungi.api.ApiConfig
import com.example.perludilindungi.model.Faskes
import com.example.perludilindungi.model.FaskesResponse
import com.example.perludilindungi.model.ProvinceCity
import com.example.perludilindungi.model.ProvinceCityResponse
import com.example.perludilindungi.utils.LocationUtils
import com.google.android.gms.location.FusedLocationProviderClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LokasiVaksinViewModel : ViewModel() {

    private val _listFaskes = MutableLiveData<ArrayList<Faskes>>()
    val listFaskes: LiveData<ArrayList<Faskes>> = _listFaskes

    private val _listProvince = MutableLiveData<ArrayList<ProvinceCity>>()
    val listProvince: LiveData<ArrayList<ProvinceCity>> = _listProvince

    private val _listCity = MutableLiveData<ArrayList<ProvinceCity>>()
    val listCity: LiveData<ArrayList<ProvinceCity>> = _listCity

    private val _selectedProvince = MutableLiveData<String>()
    val selectedProvince: LiveData<String> = _selectedProvince

    private val _selectedCity = MutableLiveData<String>()
    val selectedCity: LiveData<String> = _selectedCity

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isToast = MutableLiveData<Boolean>()
    val isToast: LiveData<Boolean> = _isToast

    private val _toastReason = MutableLiveData<String>()
    val toastReason: LiveData<String> = _toastReason

    fun getProvincesData() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getProvince()

        client.enqueue(object : Callback<ProvinceCityResponse> {
            override fun onResponse(
                call: Call<ProvinceCityResponse>,
                response: Response<ProvinceCityResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _isToast.value = true
                        _listProvince.value = responseBody.results
                    }
                } else {
                    _isToast.value = false
                    _toastReason.value = "onFailure: ${response.message()}"
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProvinceCityResponse>, t: Throwable) {
                _isLoading.value = false
                _isToast.value = false
                _toastReason.value = "onFailure: ${t.message}"
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }


    fun getCitiesData() {
        _isLoading.value = true
        val client = _selectedProvince.value?.let { ApiConfig.getApiService().getCity(it) }

        client?.enqueue(object : Callback<ProvinceCityResponse> {
            override fun onResponse(
                call: Call<ProvinceCityResponse>,
                response: Response<ProvinceCityResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _isToast.value = true
                            _listCity.value = responseBody.results
                        }
                    } else {
                        _isToast.value = false
                        _toastReason.value = "onFailure: ${response.message()}"
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProvinceCityResponse>, t: Throwable) {
                _isLoading.value = false
                _isToast.value = false
                _toastReason.value = "onFailure: ${t.message}"
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFaskesData() {
        _isLoading.value = true

        val client = _selectedCity.value?.let {
            _selectedProvince.value?.let { it1 ->
                ApiConfig.getApiService()
                    .getFaskesVaksinasi(it1, it)
            }
        }

        client?.enqueue(object : Callback<FaskesResponse> {
            override fun onResponse(
                call: Call<FaskesResponse>,
                response: Response<FaskesResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _isToast.value = true
                        _listFaskes.value = responseBody.data
                    }
                } else {
                    _isToast.value = false
                    _toastReason.value = "onFailure: ${response.message()}"
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FaskesResponse>, t: Throwable) {
                _isLoading.value = false
                _isToast.value = false
                _toastReason.value = "onFailure: ${t.message}"
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun setSelectedProvince(province: String) {
        _selectedProvince.value = province
    }


    fun setSelectedCity(city: String) {
        _selectedCity.value = city
    }

    companion object {
        private val TAG = LokasiVaksinViewModel::class.java.simpleName
    }
}