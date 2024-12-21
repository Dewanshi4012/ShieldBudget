package com.example.budgetshield.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.budgetshield.R
import com.example.budgetshield.data.SharedPref
import com.google.firebase.auth.FirebaseAuth

class FragmentSetting : Fragment(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = SharedPref(requireContext())

        // Retrieve user data
        val userEmail = sharedPref.getUserEmail()
        val userName = sharedPref.getUserName()

        // Bind UI components
        val emailTextView: TextView = view.findViewById(R.id.mail_content)
        val nameTextView: TextView = view.findViewById(R.id.name_content)
        val logoutButton: Button = view.findViewById(R.id.logout_btn)

        // Set user details
        emailTextView.text = "$userEmail"
        nameTextView.text = "$userName"

        // Set up logout button functionality
        logoutButton.setOnClickListener {
            sharedPref.clearData() // Clear shared preferences
            FirebaseAuth.getInstance().signOut() // Sign out from Firebase
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Navigate to LoginActivity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentSetting()
    }

}
