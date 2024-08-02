package RVHMisPaquetes

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ptc24.st.sendtrack.R

class ViewHolderMisPaquetes(view: View): RecyclerView.ViewHolder(view) {
    var lbPaqueteID = view.findViewById<TextView>(R.id.lbPaqueteID)
}