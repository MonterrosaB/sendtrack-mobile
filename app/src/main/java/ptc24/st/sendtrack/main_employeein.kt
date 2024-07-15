package ptc24.st.sendtrack

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import ptc24.st.sendtrack.databinding.ActivityMainEmployeeinBinding
import ptc24.st.sendtrack.databinding.ActivityMainUserBinding

class main_employeein : AppCompatActivity() {

    private lateinit var binding: ActivityMainEmployeeinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainEmployeeinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val navView: BottomNavigationView = binding.navViewEmployeeIn

        val navController = findNavController(R.id.nav_host_fragment_activity_main_employeein)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setupWithNavController(navController)

    }
}