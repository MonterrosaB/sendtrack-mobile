package ptc24.st.sendtrack

import Modelo.ClaseConexion
import Modelo.dtPaquetes
import RVHPaquetes.adaptadorPaquetes
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_Paquetes.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_Paquetes : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment__paquetes, container, false)

        val rcvPaquetes = root.findViewById<RecyclerView>(R.id.rcvPaquetes)
        rcvPaquetes.layoutManager = LinearLayoutManager(requireContext())

        fun mostrarPaquetes(): List<dtPaquetes>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.prepareStatement("select * from Paquete")!!

            val resultSet = statement.executeQuery()

            val dtPaquetes = mutableListOf<dtPaquetes>()

            while (resultSet.next()){
                val idPaquete = resultSet.getString("idPaquete")
                val peso = resultSet.getString("peso")
                val altura = resultSet.getString("Alto")
                val ancho = resultSet.getString("ancho")
                val largo = resultSet.getString("largo")

                val paquete = dtPaquetes(idPaquete, peso, altura, ancho, largo)

                dtPaquetes.add(paquete)

            }
            return dtPaquetes
        }

        CoroutineScope(Dispatchers.IO).launch {
            val paquetesDB = mostrarPaquetes()
            withContext(Dispatchers.Main){
                val adapter = adaptadorPaquetes(paquetesDB)
                rcvPaquetes.adapter = adapter
            }
        }

        return root
    }
}