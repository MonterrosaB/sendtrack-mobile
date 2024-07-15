package ptc24.st.sendtrack

import Modelo.ClaseConexion
import Modelo.dtPaqRepartidor
import Modelo.dtPaquetes
import RVHPaquetes.adaptadorPaquetes
import RVHPaquetesRepartidor.AdaptadorPaqRepartidor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
 * Use the [paquetesRepartidor.newInstance] factory method to
 * create an instance of this fragment.
 */
class paquetesRepartidor : Fragment() {
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
        val root =  inflater.inflate(R.layout.fragment_paquetes_repartidor, container, false)

        val rcvPaqueteRepartidor = root.findViewById<RecyclerView>(R.id.rcvPaquetesRepartidor)
        rcvPaqueteRepartidor.layoutManager = LinearLayoutManager(requireContext())

        fun mostrarPaquetes(): List<dtPaqRepartidor>{

            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.prepareStatement("SELECT IdPaquete, Peso, Alto, Ancho, Largo, Distrito FROM PAQUETE " +
                    "INNER JOIN DISTRITO ON Paquete.Origen = Distrito.IdDistrito")!!

            val resultSet = statement.executeQuery()

            val dtPaqRepartidor = mutableListOf<dtPaqRepartidor>()


            while (resultSet.next()){
                val Origen = resultSet.getString("Distrito")
                val idPaquete = resultSet.getString("idPaquete")
                val peso = resultSet.getString("peso")
                val altura = resultSet.getString("Alto")
                val ancho = resultSet.getString("ancho")
                val largo = resultSet.getString("largo")
                val paquete = dtPaqRepartidor(Origen ,idPaquete, peso, altura, ancho, largo)

                dtPaqRepartidor.add(paquete)
            }
            return dtPaqRepartidor
        }

        CoroutineScope(Dispatchers.IO).launch {
            val repartidorDB = mostrarPaquetes()
            withContext(Dispatchers.Main){
                val adapter = AdaptadorPaqRepartidor(repartidorDB)
                rcvPaqueteRepartidor.adapter = adapter
            }
        }

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment paquetesRepartidor.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            paquetesRepartidor().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}