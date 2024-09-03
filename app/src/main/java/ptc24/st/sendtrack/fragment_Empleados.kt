package ptc24.st.sendtrack

import Modelo.dtEmpleados
import RVHEmpleados.AdaptadorEmpleados
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class fragment_Empleados : Fragment() {


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
        val root = inflater.inflate(R.layout.fragment__empleados, container, false)

        val recyclerView = root.findViewById<RecyclerView>(R.id.rcvEmpleados)
        recyclerView.layoutManager = LinearLayoutManager(context)

        ///// TODO: Mostrar datos
        //fun obtenerEmpleados(): List<dtEmpleados>{}

        //recyclerView.layoutManager = LinearLayoutManager(context)
        //recyclerView.adapter = adapter


        return root
    }
}