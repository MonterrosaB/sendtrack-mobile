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




class fragmentMovimientos : Fragment() {
    // TODO: Rename and change types of parameters


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
        val root = inflater.inflate(R.layout.fragment_movimientos, container, false)

        val rcvMovimientos = root.findViewById<RecyclerView>(R.id.rcvMovimientos)
        rcvMovimientos.layoutManager = LinearLayoutManager(requireContext())

        fun mostrarMovimientos(): List<dtMovimientos>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.prepareStatement("select * from registrocargamento RC " +
                    "INNER JOIN ALMACEN A ON RC.IdPaquete = A.IdPaquete")!!

            val resultSet = statement.executeQuery()

            val movimientos = mutableListOf<dtMovimientos>()

            while (resultSet.next()){
                val idCargamento = resultSet.getString("IdCargamento")
                val fecha = resultSet.getString("Fecha")
                val movimiento = dtMovimientos(idCargamento, fecha)
                movimientos.add(movimiento)
            }
            return movimientos
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
}