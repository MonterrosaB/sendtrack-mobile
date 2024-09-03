package ptc24.st.sendtrack

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Objects

class rastrearPaquetes : AppCompatActivity(), OnMapReadyCallback {

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var map: GoogleMap
    private var mDatabase: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rastrear_paquetes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

            val flInformacionPaquete = findViewById<FrameLayout>(R.id.flInformacionPaquete)

            BottomSheetBehavior.from(flInformacionPaquete).apply {
            peekHeight = 100
            this.state = BottomSheetBehavior.STATE_COLLAPSED

                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@rastrearPaquetes)
                mDatabase = FirebaseDatabase.getInstance().getReference()

                if (ActivityCompat.checkSelfPermission(
                        this@rastrearPaquetes,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this@rastrearPaquetes,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                mFusedLocationClient!!.lastLocation.addOnSuccessListener { location ->
                    // Obtén la última ubicación conocida. En algunas situaciones raras, esto puede ser nulo.
                    if (location != null) {
                        println("Latitud: ${location.latitude}, Longitud: ${location.longitude}")

                        val latlang = HashMap<String, Any>()
                        latlang["latitud"] = location.latitude
                        latlang["longitud"] = location.longitude

                        mDatabase!!.child("usuarios").push().setValue(latlang)


                    }
                }


        }
    }

    private fun CreateFragment(){
        //GlobalScope.launch(Dispatchers.Main) {  }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapD) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        setMapStyle()

        GlobalScope.launch(Dispatchers.Main) {

            val limitesSV = LatLngBounds(
                LatLng(13.0669, -90.1546),  // Suroeste
                LatLng(14.4465, -87.6932)
            )

            map.moveCamera(CameraUpdateFactory.newLatLngBounds(limitesSV, 0))

            map.setLatLngBoundsForCameraTarget(limitesSV)

            map.setOnCameraMoveListener {
                val position = map.cameraPosition.target
                if (!limitesSV.contains(position)) {
                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(limitesSV, 0))
                }
            }

        }
        map.setOnMapClickListener {
        }
    }

    private fun setMapStyle() {
        val result: Boolean = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        if (!result){
            Log.e("Map", "Error set map style")
        }
    }
}