package com.mustafa.todolist

import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mustafa.todolist.databinding.RecyclerRowBinding

class ToDoAdaptor(private val toDoList: ArrayList<ToDo>) : RecyclerView.Adapter<ToDoAdaptor.ToDoHolder>()  {

    private lateinit var dataBase: SQLiteDatabase

    class ToDoHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    // Called when RecyclerView needs a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoHolder {
        // Inflate the layout for a single row in the RecyclerView
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoHolder(binding)
    }

    // Called by RecyclerView to display data at a specified position
    override fun onBindViewHolder(holder: ToDoHolder, position: Int) {
        // Bind the data to the views in the row
        holder.binding.recyclerRowTextView.text = toDoList[position].titleText

        // Set an OnCheckedChangeListener to the CheckBox
        holder.binding.recyclerRowCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Show a Toast when the CheckBox is checked
                Toast.makeText(holder.itemView.context, "You Are Done '${toDoList[position].titleText}'", Toast.LENGTH_LONG).show()
            }
        }

        // Set an OnClickListener to the entire row item
        holder.itemView.setOnClickListener {
            // Start AddToDoActivity with "old" info and the ID of the selected ToDo
            val intentToItemView = Intent(holder.itemView.context, AddToDoActivity::class.java)
            intentToItemView.putExtra("info", "old")
            intentToItemView.putExtra("id", toDoList[position].id)
            holder.itemView.context.startActivity(intentToItemView)
        }

        // This line doesn't seem to have any effect, you can remove it if unnecessary
        holder.binding.recyclerRowCheckBox
    }

    // Return the size of the data list
    override fun getItemCount(): Int {
        return toDoList.size
    }
}
