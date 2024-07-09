package RVHDireccion

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ptc24.st.sendtrack.R

class ViewHolderDireccion(view: View):RecyclerView.ViewHolder(view) {

    var lbNombreCompleto: TextView = view.findViewById(R.id.lbFullName)
    var lbDireccion: TextView = view.findViewById(R.id.lbAddress)
    val lbEditar: TextView = view.findViewById(R.id.lbEdit)
    val lbEliminar: TextView = view.findViewById(R.id.lbDelete)
    val cardDireccion: MaterialCardView = view.findViewById(R.id.direccionCard)
}