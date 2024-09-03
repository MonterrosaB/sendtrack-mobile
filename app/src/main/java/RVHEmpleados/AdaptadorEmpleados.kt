package RVHEmpleados

import Modelo.ClaseConexion
import Modelo.dtEmpleados
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ptc24.st.sendtrack.R


class AdaptadorEmpleados(var Datos: List<dtEmpleados>):RecyclerView.Adapter<ViewHolderEmpleados>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderEmpleados {
        //Unir el Recyclerview con la card
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolderEmpleados(vista)

    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderEmpleados, position: Int) {
        //Informacion a la card
        val item = Datos[position]
        holder.lbNombre.text = item.nombre + " " +item.apellidoPa
        holder.lbCorreo.text = item.Email
        holder.lbTelefono.text = item.Telefono
        holder.lbRol.text = item.Rol.toString()
    }


}