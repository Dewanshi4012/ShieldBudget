package com.example.budgetshield.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgetshield.R
import com.example.budgetshield.data.RecordModel
import com.example.budgetshield.databinding.FragmentDashboardBinding
import com.example.budgetshield.viewmodel.RecordViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class FragmentDash : Fragment() {

    lateinit var listAdapter: ListAdapter
    lateinit var mContext: Context
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var recordViewModel: RecordViewModel
    lateinit var dashboardBinding: FragmentDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recordViewModel = ViewModelProvider(requireActivity()).get(RecordViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false)
        return dashboardBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashboardBinding.recyclerMember.layoutManager = LinearLayoutManager(mContext)

        // Observe the records and update the RecyclerView when it changes
        recordViewModel.records.observe(viewLifecycleOwner, Observer { records ->
            updateRecyclerView(records)
        })

        // Fetch data from Firestore when the fragment is created
        fetchDataFromFirestore()

        // Button to navigate to the expense fragment
        dashboardBinding.AddNewbutton.setOnClickListener {
            navigateToExpenseFragment()
        }
    }

    // This method is called every time the fragment is resumed (when it becomes visible again)
    override fun onResume() {
        super.onResume()
        fetchDataFromFirestore() // Fetch data again when the fragment is resumed
    }

    // Fetch data from Firestore and update the ViewModel
    private fun fetchDataFromFirestore() {
        // Clear the existing data in the ViewModel before fetching new data
        recordViewModel.clearRecords()

        val userId = auth.currentUser?.uid ?: return
        val recordsRef = db.collection(userId)

        recordsRef.get()
            .addOnSuccessListener { querySnapshot ->
                val records = mutableListOf<RecordModel>()

                // Extract data from Firestore documents
                for (document: QueryDocumentSnapshot in querySnapshot) {
                    val recordType = document.getString("type") ?: ""
                    val dateOfRecord = document.getString("date") ?: ""
                    val description = document.getString("description") ?: ""
                    val amountPrice = document.getLong("amount") ?: 0L

                    // Add the RecordModel to the list
                    records.add(RecordModel(recordType, dateOfRecord, description, amountPrice.toString()))
                }

                // Update the ViewModel with the fetched records
                recordViewModel.addRecord(records)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error fetching documents: $exception", Toast.LENGTH_SHORT).show()
            }
    }

    // Update the RecyclerView with the new records
    private fun updateRecyclerView(records: List<RecordModel>) {
        if (::listAdapter.isInitialized) {
            listAdapter.updateRecords(records)
        } else {
            listAdapter = ListAdapter(records) { record ->
                // Handle item click by navigating to the expense details fragment
                navigateToExpenseFragment(record)
            }
            dashboardBinding.recyclerMember.adapter = listAdapter
        }
    }

    // Navigate to the Expense details fragment with the selected record
    private fun navigateToExpenseFragment(record: RecordModel) {
        val fragmentExpense = FragmentDesExpense.newInstance(record)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragmentExpense)
            .addToBackStack(null) // Adds this transaction to the back stack
            .commit()
    }

    // Navigate to a new expense entry fragment
    private fun navigateToExpenseFragment() {
        val fragmentExpense = FragmentExpense.newInstance()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragmentExpense)
            .addToBackStack(null) // Adds this transaction to the back stack
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentDash()
    }
}