package RVHEmpleados

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ptc24.st.sendtrack.R

class ViewHolderEmpleados(view: View) : RecyclerView.ViewHolder(view) {

    val lbNombre = view.findViewById<TextView>(R.id.lbNombre)
    val lbCorreo = view.findViewById<TextView>(R.id.lbDetalleC)
    val lbTelefono = view.findViewById<TextView>(R.id.lbDetalleT)
    val lbRol = view.findViewById<TextView>(R.id.lbDetalleR)
}