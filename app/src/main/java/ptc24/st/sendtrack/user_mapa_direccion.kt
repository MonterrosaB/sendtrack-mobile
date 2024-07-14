package ptc24.st.sendtrack

import Modelo.ClaseConexion
import Modelo.dtDistrito
import Modelo.dtMunicipio
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import java.util.Locale

class user_mapa_direccion : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_mapa_direccion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun CreateFragment(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapD) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        setMapStyle()

        map.setOnCameraIdleListener {

            val india = LatLng(28.5021359, 77.4054901)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(india,17F))

            val lat = map.cameraPosition.target.latitude
            val lng = map.cameraPosition.target.longitude
            val addressTV = findViewById<TextView>(R.id.tv)

            val mGeocoder = Geocoder(applicationContext, Locale.getDefault())
            var addressString= ""

            try{
                val addressList: MutableList<android.location.Address>? = mGeocoder.getFromLocation(lat, lng, 1)

                if (addressList != null && addressList.isNotEmpty()){
                    val address = addressList[0]
                    val sb = StringBuilder()
                    for (i in 0 until address.maxAddressLineIndex) {
                        sb.append(address.getAddressLine(i)).append("\n")
                    }

                    if (address.premises != null)
                        sb.append(address.premises).append(", ")

                    sb.append(address.subAdminArea).append("\n")
                    sb.append(address.locality).append(", ")
                    sb.append(address.adminArea).append(", ")
                    sb.append(address.countryName).append(", ")
                    sb.append(address.postalCode)

                    addressString = sb.toString()
                }
            }
            catch (e: Exception){
                Toast.makeText(applicationContext, "$e", Toast.LENGTH_SHORT).show()
            }
            addressTV.text = "Lat: $lat \nLng: $lng \nAddress: $addressString"
        }
    }


    private fun setMapStyle() {
        val result: Boolean = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        if (!result){
            Log.e("Map", "Error set map style")
        }
    }
}