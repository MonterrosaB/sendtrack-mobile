package RVHPaquetes

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ptc24.st.sendtrack.R

class ViewHolderPaquetes(view: View): RecyclerView.ViewHolder(view) {
    val lblIdPaquete = view.findViewById<TextView>(R.id.lblIdPaquete)
    val lblPesoPaquete = view.findViewById<TextView>(R.id.lblPesoPaquete)
    val lblAlturaPaquete = view.findViewById<TextView>(R.id.lblAlturaPaquete)
    val lblAnchoPaquete = view.findViewById<TextView>(R.id.lblAnchoPaquete)
    val lblLargoPaquete = view.findViewById<TextView>(R.id.lblLargoPaquete)
}