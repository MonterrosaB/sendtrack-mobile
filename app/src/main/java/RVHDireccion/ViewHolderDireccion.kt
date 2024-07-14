package RVHDireccion

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ptc24.st.sendtrack.R

class ViewHolderDireccion(view: View):RecyclerView.ViewHolder(view) {

    var lbNombreCompleto: TextView = view.findViewById(R.id.lbFullName)
    var lbDireccion: TextView = view.findViewById(R.id.lbAddress)
    val lbEditar: TextView = view.findViewById(R.id.lbEdit)
    val lbEliminar: TextView = view.findViewById(R.id.lbDelete)
}