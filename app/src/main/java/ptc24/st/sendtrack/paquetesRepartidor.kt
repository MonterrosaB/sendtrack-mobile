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

class paquetesRepartidor : Fragment() {

    companion object variableGlobalRepartidor {
        var ruta: String = ""
    }

    var obtenerRuta: String = ""


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
        val root =  inflater.inflate(R.layout.fragment_paquetes_repartidor, container, false)
        val rcvPaqueteRepartidor = root.findViewById<RecyclerView>(R.id.rcvPaquetesRepartidor)
        rcvPaqueteRepartidor.layoutManager = LinearLayoutManager(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            obtenerRuta = obetenerIdRuta().toString()
        }



        fun mostrarPaquetes(): List<dtPaqRepartidor>{

            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.prepareStatement("Select P.IdPaquete, Peso, Alto, Ancho, Largo, Distrito " +
                    "from Paquete P " +
                    "INNER JOIN Distrito D ON D.IdDistrito = P.Origen " +
                    "where IdPaquete in " +
                    "(select IdPaquete from RegistroContenedor RC " +
                    "INNER JOIN Contenedor C ON C.IdContenedor = RC.IdContenedor " +
                    "WHERE C.IdRuta = ?) ")!!

            statement.setString(1, ruta)

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


        if(!obtenerRuta.isNullOrEmpty()){
            ruta = obtenerRuta

           CoroutineScope(Dispatchers.IO).launch {
               val repartidorDB = mostrarPaquetes()
               withContext(Dispatchers.Main){
                   val adapter = AdaptadorPaqRepartidor(repartidorDB)
                   rcvPaqueteRepartidor.adapter = adapter
               }
           }
       }
        return root
    }

  private fun obetenerIdRuta(): String? {

        var rutaId: String? = null

        val objConexion = ClaseConexion().cadenaConexion()

        val statement = objConexion?.prepareStatement("SELECT IdRuta FROM Ruta " +
                "WHERE Finalizado = 0 AND IdUsuario = ? AND ROWNUM = 1 ORDER BY IdRuta DESC")!!

        statement.setString(1, Login.idUser)

        val resultSet = statement.executeQuery()

        while (resultSet.next()){
            rutaId = resultSet.getString("IdRuta")
        }
        return rutaId
    }
}