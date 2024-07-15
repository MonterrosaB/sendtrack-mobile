package ptc24.st.sendtrack

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class employee_ruta : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var btnCalculate: Button

    private var start = ""
    private var end = ""

    var poly: Polyline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val root = inflater.inflate(R.layout.fragment_employee_ruta, container, false)
        btnCalculate = root.findViewById(R.id.btnRuta)
        btnCalculate.setOnClickListener {
            start = ""
            end = ""
            poly?.remove()
            poly = null
            Toast.makeText(requireContext(), "Calculando ruta", Toast.LENGTH_SHORT).show()
            if (::map.isInitialized) {
                map.setOnMapClickListener {
                    if (start.isNotEmpty()) {
                        start = "${it.longitude},${it.latitude}"
                    } else if (end.isEmpty()) {
                        end = "${it.longitude},${it.latitude}"
                        createRoute()
                    }


                }
            }
        }
        return root


    }


    private fun CreateFragment() {

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }

    private fun createRoute() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiService::class.java)
                .getRoute("5b3ce3597851110001cf6248a4650ebb0cc840bba5eb421c0de75307", start, end)
            if (call.isSuccessful) {
                drawRoute(call.body())

            } else {

                Log.i("aris", "no funciona")

            }

        }
    }

    fun drawRoute(routeResponse: RouteResponse?) {
        val PolylineOptions = PolylineOptions()
        routeResponse?.features?.first()?.geometry?.coordinates?.forEach {
            PolylineOptions.add(LatLng(it[0], it[1]))
        }
        poly = map.addPolyline(PolylineOptions)
    }


    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}












