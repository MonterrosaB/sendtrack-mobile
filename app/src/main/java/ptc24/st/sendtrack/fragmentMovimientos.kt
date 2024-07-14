package ptc24.st.sendtrack

import Modelo.ClaseConexion
import Modelo.dtMovimientos
import RVHMovimientos.AdaptadorMovimientos
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
 * Use the [fragmentMovimientos.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragmentMovimientos : Fragment() {
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
        val root = inflater.inflate(R.layout.fragment_movimientos, container, false)

        val rcvMovimientos = root.findViewById<RecyclerView>(R.id.rcvMovimientos)
        rcvMovimientos.layoutManager = LinearLayoutManager(requireContext())

        fun mostrarMovimientos(): List<dtMovimientos>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.prepareStatement("Select *  from Almacen")!!

            val resultSet = statement.executeQuery()

            val dtMovimientos = mutableListOf<dtMovimientos>()

            while (resultSet.next()){
                val idCargamento = resultSet.getString("idCargamento")
                val horaEntrada = resultSet.getString("horaEntrada")
                val movimiento = dtMovimientos(horaEntrada, idCargamento)

                dtMovimientos.add(movimiento)
            }
            return mostrarMovimientos()


        }

        CoroutineScope(Dispatchers.IO).launch {
            val movimientosDB = mostrarMovimientos()
            withContext(Dispatchers.Main){
                val adapter = AdaptadorMovimientos(movimientosDB)
                rcvMovimientos.adapter = adapter
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
         * @return A new instance of fragment fragmentMovimientos.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragmentMovimientos().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}