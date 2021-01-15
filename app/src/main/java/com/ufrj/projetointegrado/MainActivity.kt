package com.ufrj.projetointegrado

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.ufrj.projetointegrado.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.myToolbar)
    }
    fun showUpButton() {  // Mostra seta para voltar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun hideUpButton() {  // Esconde seta para voltar
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean{
        val navController = this.findNavController(R.id.navigation_fragment)
        return navController.navigateUp()
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.overflow_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val navController: NavController = Navigation.findNavController(this, R.id.navigation_fragment)
//        return when (item.itemId) {
//            R.id.sobre_id -> {
//                NavigationUI.onNavDestinationSelected(item, navController)
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

}