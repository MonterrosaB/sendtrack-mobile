package RVHPaquetes

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ptc24.st.sendtrack.R

class ViewHolderPaquetes(view: View): RecyclerView.ViewHolder(view) {
    val lblIdPaquete = view.findViewById<TextView>(R.id.lblIdPaquete)
    val lbSeccion = view.findViewById<TextView>(R.id.lbSeccionP)
    val lbCargamento = view.findViewById<TextView>(R.id.lblCargamentoP)
    val lbPeso = view.findViewById<TextView>(R.id.lbPesoP)
}