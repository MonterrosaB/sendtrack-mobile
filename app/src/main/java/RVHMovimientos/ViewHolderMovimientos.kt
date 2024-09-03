package RVHMovimientos

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ptc24.st.sendtrack.R

class ViewHolderMovimientos(view: View): RecyclerView.ViewHolder(view) {
    val lblHoraEntrada = view.findViewById<TextView>(R.id.lblHoraEntrada)
    val lblidCodigoCargamento = view.findViewById<TextView>(R.id.lblidCargamento)
    val lbNumeroPaquetes = view.findViewById<TextView>(R.id.lbNPaquetes)
    val lbSeccion = view.findViewById<TextView>(R.id.lbSeccion)
}