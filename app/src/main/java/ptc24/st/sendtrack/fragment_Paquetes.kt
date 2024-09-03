package ptc24.st.sendtrack

import Modelo.ClaseConexion
import Modelo.dtMovimientos
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

class fragment_Paquetes : Fragment() {

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
        val root = inflater.inflate(R.layout.fragment__paquetes, container, false)

        val rcvPaquetes = root.findViewById<RecyclerView>(R.id.rcvPaquetes)
        rcvPaquetes.layoutManager = LinearLayoutManager(requireContext())

        fun mostrarPaquetes(): List<dtPaquetes>{

            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.prepareStatement("Select P.IdPaquete, P.Peso ,RC.IdCargamento , S.Nombre from RegistroCargamento RC " +
                    "INNER JOIN CentroRecoleccion CR on CR.IdPaquete = RC.IdPaquete " +
                    "INNER JOIN Almacen A ON a.idpaquete = rc.idpaquete " +
                    "INNER JOIN Seccion S ON s.idseccion = a.idseccion " +
                    "INNER JOIN Paquete P ON P.IdPaquete = CR.IdPaquete " +
                    "where IdCargamento in " +
                    "(select IdCargamento from Almacen A " +
                    "INNER JOIN RegistroCargamento RC ON A.IdPaquete = RC.IdPaquete " +
                    "group by RC.IdCargamento) " +
                    "and s.idsucursal = ? and  TRUNC(A.Fecha) = TRUNC(SYSDATE) ORDER BY IDCargamento ASC ")!!

            statement.setString(1, Login.sucursal)

            val resultSet = statement.executeQuery()

            val paquetes = mutableListOf<dtPaquetes>()

            while (resultSet.next()){
                val idPaquete = resultSet.getString("IdPaquete")
                val seccion = resultSet.getString("Nombre")
                val cargamento = resultSet.getString("IdCargamento")
                val peso = resultSet.getString("Peso")
                val paquete = dtPaquetes(idPaquete, seccion, cargamento, peso)
                paquetes.add(paquete)
            }
            return paquetes
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