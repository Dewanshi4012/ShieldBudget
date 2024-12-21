package com.example.budgetshield.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.budgetshield.data.RecordModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RecordViewModel : ViewModel() {
    private val _records = MutableLiveData<MutableList<RecordModel>>()
    val records: LiveData<MutableList<RecordModel>> get() = _records

    init {
        _records.value = mutableListOf() // Initialize with an empty list
    }

    // Add multiple records
    fun addRecord(records: List<RecordModel>) {
        _records.value?.let {
            // Avoid duplicate records by checking for the presence of the same record
            val uniqueRecords = records.filterNot { newRecord ->
                it.any { existingRecord -> existingRecord == newRecord }
            }
            it.addAll(uniqueRecords)
            _records.value = it // Notify observers of the change
        }
    }

    // Clear all records
    fun clearRecords() {
        _records.value = mutableListOf() // Clear the current list
    }

    // Delete a single record
    fun deleteRecord(record: RecordModel) {
        // Remove from local list
        _records.value?.let {
            it.remove(record)
            _records.value = it // Notify observers of the change
        }

        // Delete from Firestore
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val recordsRef = FirebaseFirestore.getInstance().collection(userId)
            recordsRef
                .whereEqualTo("type", record.type)
                .whereEqualTo("date", record.date)
                .whereEqualTo("description", record.description)
                .whereEqualTo("amount", record.amount.toLongOrNull())
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        recordsRef.document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                println("Record deleted successfully from Firestore.")
                            }
                            .addOnFailureListener { e ->
                                println("Error deleting record: $e")
                            }
                    }
                }
                .addOnFailureListener { e ->
                    println("Error finding record to delete: $e")
                }
        }
    }
}