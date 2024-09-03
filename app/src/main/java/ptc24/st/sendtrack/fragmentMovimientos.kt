package ptc24.st.sendtrack

import Modelo.ClaseConexion
import Modelo.dtMovimientos
import RVHMovimientos.AdaptadorMovimientos
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class fragmentMovimientos : Fragment() {


    private lateinit var btnIngresarCargamentos: Button

    companion object variableMovimientos {
        lateinit var idCargamentoInhgresado: String
    }


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
        btnIngresarCargamentos = root.findViewById(R.id.btnIngresarCargamento)
        rcvMovimientos.layoutManager = LinearLayoutManager(requireContext())


        fun mostrarMovimientos(): List<dtMovimientos>{

            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.prepareStatement("Select RC.IdCargamento , count(RC.IdPaquete) as NumPaquetes, TO_CHAR(A.Fecha, 'HH24:MI') as Fecha, S.Nombre " +
                    "from RegistroCargamento RC " +
                    "INNER JOIN CentroRecoleccion CR on CR.IdPaquete = RC.IdPaquete " +
                    "INNER JOIN Almacen A ON A.IdPaquete = RC.IdPaquete " +
                    "INNER JOIN Seccion S ON S.idseccion = A.IdSeccion " +
                    "where IdCargamento in " +
                    "(select IdCargamento from Almacen A " +
                    "INNER JOIN RegistroCargamento RC ON A.IdPaquete = RC.IdPaquete " +
                    "group by RC.IdCargamento) " +
                    "and S.IdSucursal = ? " +
                    "and TRUNC(A.Fecha) = TRUNC(SYSDATE) " +
                    "group by RC.IdCargamento, A.Fecha,  S.Nombre")!!

            statement.setString(1, Login.sucursal)

            val resultSet = statement.executeQuery()

            val movimientos = mutableListOf<dtMovimientos>()

            while (resultSet.next()){
                val idCargamento = resultSet.getString("IdCargamento")
                val fecha = resultSet.getString("Fecha")
                val numPaquete = resultSet.getString("NumPaquetes")
                val seccion = resultSet.getString("Nombre")
                val movimiento = dtMovimientos(idCargamento, fecha, numPaquete, seccion)
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

        btnIngresarCargamentos.setOnClickListener {

            val txtCargamento = root.findViewById<EditText>(R.id.txtCodigoCargamento)


            if (txtCargamento.text.isEmpty()){
                Toast.makeText(requireContext(), "El campo debe estar lleno", Toast.LENGTH_SHORT).show()
            }
            else{
                idCargamentoInhgresado = txtCargamento.text.toString()
                val bottomSheetFragment = employee_Secciones()
                bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
                CoroutineScope(Dispatchers.IO).launch {
                    val movimientosDB = mostrarMovimientos()
                    withContext(Dispatchers.Main){
                        val adapter = AdaptadorMovimientos(movimientosDB)
                        rcvMovimientos.adapter = adapter
                    }
                }
            }
        }
        return root
    }
}