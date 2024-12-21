package com.example.budgetshield.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetshield.R
import com.example.budgetshield.data.RecordModel

class ListAdapter(private var records: List<RecordModel>, private val onItemClick: (RecordModel) -> Unit) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    fun updateRecords(newRecords: List<RecordModel>) {
        records = newRecords
        notifyDataSetChanged()  // This will notify the RecyclerView to refresh with the new data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_member, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = records[position]
        holder.descriptionTextView.text = record.description
        holder.recordTypeTextView.text = record.type
        holder.amountTextView.text = record.amount
        holder.dateTextView.text = record.date

        holder.itemView.setOnClickListener {
            onItemClick(record) // Trigger the onItemClick lambda with the clicked record
        }
    }

    override fun getItemCount(): Int = records.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descriptionTextView: TextView = itemView.findViewById(R.id.Description)
        val recordTypeTextView: TextView = itemView.findViewById(R.id.EorI)
        val amountTextView: TextView = itemView.findViewById(R.id.price)
        val dateTextView: TextView = itemView.findViewById(R.id.date)
    }
}
