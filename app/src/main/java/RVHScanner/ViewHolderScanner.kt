package RVHScanner

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ptc24.st.sendtrack.R

class ViewHolderScanner(view: View): RecyclerView.ViewHolder(view) {
    var lbIndicacion: TextView = view.findViewById(R.id.lbIndicacion)
    var lbPaquete: TextView = view.findViewById(R.id.lbCodigoPaquete)
    val lbDireccion: TextView = view.findViewById(R.id.lbDireccion)
}