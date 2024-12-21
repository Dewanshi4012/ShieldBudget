package com.example.budgetshield.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.budgetshield.R
import com.example.budgetshield.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_dashboard -> {
                    inflateFragment(FragmentDash.newInstance())
                }

                R.id.nav_home -> {
                    inflateFragment(FragmentSetting.newInstance())
                }
            }
            true
        }
        binding.bottomBar.selectedItemId = R.id.nav_dashboard

    }

    private fun inflateFragment(newInstance: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, newInstance)
        transaction.commit()
    }
}
