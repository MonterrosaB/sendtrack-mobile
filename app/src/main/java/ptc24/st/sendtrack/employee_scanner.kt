package ptc24.st.sendtrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ptc24.st.sendtrack.databinding.FragmentEmployeeScannerBinding

class employee_scanner : Fragment() {

    private var _binding: FragmentEmployeeScannerBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentEmployeeScannerBinding.inflate(inflater, container, false)
        val root = binding.root

        val btnSiguiente = binding.btnSiguiente
        val txtCodigo = binding.txtEditCodigoPaqueteR.text.toString()

        return root
    }

}