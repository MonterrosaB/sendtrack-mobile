package ptc24.st.sendtrack

import Modelo.ClaseConexion
import RVHScanner.AdaptadorScanner
import android.health.connect.datatypes.ExerciseRoute
import android.location.Location
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.Response
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ptc24.st.sendtrack.paquetesRepartidor.variableGlobalRepartidor.ruta
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class employee_ruta : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var btnCalculate: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient


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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())





        CreateFragment()



        CoroutineScope(Dispatchers.IO).launch {
            val (lat, lang) = getLocationPackage()

            println("latitud: $lat, Longitud: $lang")
        }


        btnCalculate = root.findViewById(R.id.btnRuta)

        btnCalculate.setOnClickListener {
            start = ""  // Replace with your start coordinates
            end = ""   // Replace with your end coordinates

            poly?.remove()
            poly = null
            Toast.makeText(requireContext(), "Calculando ruta", Toast.LENGTH_SHORT).show()
            if (::map.isInitialized) {
                map.setOnMapClickListener {

                    if (start.isEmpty()) {
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

    private fun getLocationPackage(): Pair<String?, String?> {

        var lat: String? = null
        var lon: String? = null

        val objConexion = ClaseConexion().cadenaConexion()

        val statement = objConexion?.prepareStatement("Select P.IdPaquete, " +
                "D.Ubicacion.SDO_POINT.X AS lon, " +
                "D.Ubicacion.SDO_POINT.Y AS lat " +
                "from Paquete P " +
                "INNER JOIN Direccion D ON D.IdDireccion = P.IdDireccion " +
                "where IdPaquete in (SELECT RC.IdPaquete FROM RegistroContenedor RC " +
                "    INNER JOIN Contenedor C ON C.IdContenedor = RC.IdContenedor " +
                "    WHERE C.IdRuta = ? ORDER BY RC.IndexEntrega ASC " +
                "    FETCH FIRST 1 ROWS ONLY) ")!!

        statement.setString(1, ruta)

        val resultSet = statement.executeQuery()

        while (resultSet.next()){
            lat = resultSet.getString("Lat")
            lon = resultSet.getString("Lon")
        }
        return Pair(lat, lon)
    }


    private fun CreateFragment() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapRutas) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
      map = googleMap

       GlobalScope.launch(Dispatchers.Main) {

            val limitesSV = LatLngBounds(
                LatLng(13.0669, -90.1546),
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
    }

    private fun createRoute() {
        CoroutineScope(Dispatchers.IO).launch {
            val callRoute = getRetrofit().create(ApiService::class.java)
                .getRoute("5b3ce3597851110001cf6248ff4949cb33284602b692c72b3737e052", start, end)

            if (callRoute.isSuccessful) {
                drawRoute(callRoute.body())
            } else {
                Log.e("Error", "Failed to fetch route: ${callRoute.errorBody()?.string()}")            }

        }
    }

    private fun drawRoute(routeResponse: RouteResponse?) {
        val PolylineOptions = PolylineOptions()

        routeResponse?.features?.first()?.geometry?.coordinates?.forEach {
            PolylineOptions.add(LatLng(it[1], it[0]))
        }
        activity?.runOnUiThread {
            poly = map.addPolyline(PolylineOptions)
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}












