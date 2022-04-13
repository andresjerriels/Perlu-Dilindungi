package com.example.perludilindungi.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.example.perludilindungi.R
import com.example.perludilindungi.api.ApiConfig
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_scan.*
import retrofit2.Call
import retrofit2.Response

class ScanActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var codescanner: CodeScanner
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val LOCATION_REQUEST_CODE = 100;

    private var sensorSuhu: Sensor? = null
    private var userLat : Double = 0.0
    private var userLong : Double = 0.0
    private var isSensorSuhuAvailable : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        val actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "Scan QR Code"
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        iv_status.visibility = INVISIBLE
        resultrequest.visibility = INVISIBLE
        resultscan.visibility = INVISIBLE

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorSuhu = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        if (sensorSuhu != null){
            isSensorSuhuAvailable = true
        } else {
            Toast.makeText(this, "Sensor suhu tidak ditemukan pada perangkat Anda",
                Toast.LENGTH_SHORT).show()
            isSensorSuhuAvailable = false
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),123)
        } else {
            startScanning()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startScanning(){
        val scannerView: CodeScannerView =findViewById(R.id.scanner_view)
        codescanner = CodeScanner(this,scannerView)
        codescanner.camera = CodeScanner.CAMERA_BACK
        codescanner.formats = CodeScanner.ALL_FORMATS
        codescanner.autoFocusMode = AutoFocusMode.SAFE
        codescanner.scanMode = ScanMode.SINGLE
        codescanner.isAutoFocusEnabled = true
        codescanner.isFlashEnabled = false

        codescanner.decodeCallback = DecodeCallback {
        //    runOnUiThread{
        //        Toast.makeText(this,"Scan Result: ${it.text}", Toast.LENGTH_SHORT).show()
        //    }
            startCheckInProcess(it.text)
        }
        codescanner.errorCallback = ErrorCallback {
            runOnUiThread{
                Toast.makeText(this,"Camera initialization error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        scannerView.setOnClickListener{
            codescanner.startPreview()
        }
    }

    private fun startCheckInProcess(s : String) {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)

            }
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            if (location != null) {
                userLat = location.latitude
                userLong = location.longitude

                getAPIResponse(s)

            } else {
                Toast.makeText(this, "Gagal mendapatkan lokasi Anda", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun getAPIResponse(qrCode : String) {
        val checkinreq = CheckInRequest(qrCode, userLat, userLong)
        val postdata = ApiConfig.getApiService().checkIn(checkinreq)

        postdata.enqueue(object: retrofit2.Callback<CheckInResponse> {
            override fun onFailure(call: Call<CheckInResponse>, t: Throwable){
                resultrequest.text = "FAILED"
            }
            override fun onResponse(call: Call<CheckInResponse>, response: Response<CheckInResponse>){
                val respon = response.body()
                addResponse(respon)
            }
        })
    }

    private fun addResponse(respon: CheckInResponse?) {
        iv_status.visibility = VISIBLE
        resultrequest.visibility = VISIBLE
        resultscan.visibility = VISIBLE

        if (respon?.success.toString() == "null") {
            iv_status.setImageResource(R.drawable.ic_cross_red_24)
            resultscan.text = "Gagal"
            resultrequest.text = respon?.success.toString()
        } else {
            if (respon?.data!!.userStatus == "red" || respon.data!!.userStatus == "black"){
                iv_status.setImageResource(R.drawable.ic_cross_red_24)
                resultscan.text = "Gagal"
                resultrequest.text = respon.data!!.reason.toString()
            } else {
                iv_status.setImageResource(R.drawable.ic_check_circle_green_24)
                resultscan.text = "Berhasil"
                resultrequest.text = respon.data!!.reason.toString()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
                startScanning()
            } else {
                finish()
            }
        } else if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        val derajatSuhu = event.values[0]
        // Do something with this sensor data.
        suhu.text = "${derajatSuhu}°C"
    }

    override fun onResume() {
        if (isSensorSuhuAvailable){
            sensorManager.registerListener(this, sensorSuhu, SensorManager.SENSOR_DELAY_NORMAL)
        }
        if (::codescanner.isInitialized){
            codescanner.startPreview()
        }
        super.onResume()
    }

    override fun onPause() {
        if (isSensorSuhuAvailable){
            sensorManager.unregisterListener(this)
        }
        if (::codescanner.isInitialized){
            codescanner.releaseResources()
        }
        super.onPause()
    }
}