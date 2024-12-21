package com.example.budgetshield.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.budgetshield.R
import com.example.budgetshield.data.RecordModel
import com.example.budgetshield.databinding.ExpenseDesBinding
import com.example.budgetshield.viewmodel.RecordViewModel

class FragmentDesExpense: Fragment() {
    private lateinit var record: RecordModel
    private lateinit var binding: ExpenseDesBinding
    private lateinit var recordViewModel: RecordViewModel
    companion object {
        @JvmStatic
        fun newInstance(record: RecordModel) = FragmentDesExpense().apply {
            arguments = Bundle().apply {
                putSerializable("record", record) // Pass the RecordModel as an argument
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recordViewModel = ViewModelProvider(requireActivity()).get(RecordViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ExpenseDesBinding.inflate(inflater, container, false)

        // Get the RecordModel from arguments
        record = arguments?.getSerializable("record") as? RecordModel
            ?: return binding.root

        // Populate the UI with the record details
        binding.descript.text = record.description
        binding.expense.text = record.type
        binding.amount.text = record.amount
        binding.ExDate.text = record.date

        // Set up the delete icon listener
        binding.delete.setOnClickListener {
            // Call the ViewModel to delete the record
            recordViewModel.deleteRecord(record)

            // Close the fragment
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }


}
