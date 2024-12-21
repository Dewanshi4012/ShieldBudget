package com.example.budgetshield.view

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.budgetshield.R
import com.example.budgetshield.databinding.FragmentNewExpenseBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class FragmentExpense : Fragment() {

    lateinit var mContext: Context
    lateinit var expenseBinding: FragmentNewExpenseBinding

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        expenseBinding = FragmentNewExpenseBinding.inflate(inflater, container, false)
        return expenseBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        expenseBinding.ivCalendar.setOnClickListener {
            showDatePickerDialog()
        }

        expenseBinding.savebtn.setOnClickListener {
            saveExpenseData()
        }

        expenseBinding.arrow.setOnClickListener {
            navigateToExpenseFragment()
        }
    }

    private fun saveExpenseData() {
        val selectedRadioButtonId = expenseBinding.radiogroup.checkedRadioButtonId
        val typeOfRecord = when (selectedRadioButtonId) {
            R.id.rd1 -> "expense"
            R.id.rd2 -> "income"
            else -> "nullA"
        }

        val selectedDate = expenseBinding.tvDate.text.toString().trim()
        val description = expenseBinding.etDescription.text.toString().trim()
        val amount = expenseBinding.amount.text.toString().toDoubleOrNull()

        if (typeOfRecord.isEmpty() || selectedDate.isEmpty() || description.isEmpty() || amount == null) {
            Toast.makeText(mContext, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUser = auth.currentUser
        val userId = currentUser?.uid ?: return

        val userCollection = db.collection(userId)

        userCollection.get().addOnSuccessListener { querySnapshot ->
            val nextDocId = (querySnapshot.size() + 1).toString()

            val expenseData = hashMapOf(
                "type" to typeOfRecord,
                "date" to selectedDate,
                "description" to description,
                "amount" to amount
            )

            userCollection.document(nextDocId).set(expenseData)
                .addOnSuccessListener {
                    Toast.makeText(mContext, "Expense saved successfully", Toast.LENGTH_SHORT).show()
                    navigateToExpenseFragment()
                }
                .addOnFailureListener {
                    Toast.makeText(mContext, "Failed to save data", Toast.LENGTH_SHORT).show()
                }

        }.addOnFailureListener {
            Toast.makeText(mContext, "Failed to fetch data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                expenseBinding.tvDate.text = selectedDate
            }, year, month, day
        )
        datePickerDialog.show()
    }

    private fun navigateToExpenseFragment() {
        val fragmentDash = FragmentDash.newInstance()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragmentDash)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentExpense()
    }
}
