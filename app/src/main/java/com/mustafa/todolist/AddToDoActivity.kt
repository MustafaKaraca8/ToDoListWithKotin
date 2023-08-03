package com.mustafa.todolist

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mustafa.todolist.databinding.ActivityAddToDoBinding
import java.lang.Exception

class AddToDoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddToDoBinding
    private lateinit var dataBase: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddToDoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Open or create the "ToDos" database
        dataBase = this.openOrCreateDatabase("ToDos", MODE_PRIVATE, null)

        // Show the data for editing or new ToDos
        showInfoData()
    }

    // Show the data for editing an existing ToDos or creating a new one
    private fun showInfoData() {
        val intent = intent
        val info = intent.getStringExtra("info")

        if (info.equals("new")) {
            // Clear the text fields and set appropriate button visibility for new ToDos
            binding.toDoText.setText("")
            binding.titleText.setText("")
            binding.saveButton.visibility = View.VISIBLE
            binding.deleteButton.visibility = View.INVISIBLE
        } else {
            // Disable text fields and set appropriate button visibility for existing ToDos
            binding.saveButton.visibility = View.INVISIBLE
            binding.deleteButton.visibility = View.VISIBLE
            binding.toDoText.isEnabled = false
            binding.titleText.isEnabled = false

            // Retrieve the ID of the selected ToDos from the intent
            val selectedRecyclerRowItem = intent.getIntExtra("id", 1)

            // Retrieve the data for the selected ToDos from the database
            val cursor = dataBase.rawQuery("SELECT * FROM todos WHERE id = ?", arrayOf(selectedRecyclerRowItem.toString()))
            val titleIx = cursor.getColumnIndex("title")
            val contentIx = cursor.getColumnIndex("content")

            // Set the retrieved data to the corresponding text fields
            while (cursor.moveToNext()) {
                binding.titleText.setText(cursor.getString(titleIx))
                binding.toDoText.setText(cursor.getString(contentIx))
            }
            cursor.close()
        }
    }

    // Save or update the ToDos item to the database
    fun save(view: View) {
        val titleText = binding.titleText.text.toString()
        val contentText = binding.toDoText.text.toString()

        if (titleText.isNotBlank() && contentText.isNotBlank()) {
            try {
                // Create the "todos" table if it doesn't exist
                dataBase.execSQL("CREATE TABLE IF NOT EXISTS todos(id INTEGER PRIMARY KEY,title VARCHAR, content TEXT)")

                // Insert the new ToDos into the database
                val sqlString = "INSERT INTO todos(title , content) VALUES (? , ?)"
                val statement = dataBase.compileStatement(sqlString)
                statement.bindString(1, titleText)
                statement.bindString(2, contentText)
                statement.execute()

            } catch (e: Exception) {
                // Show an error message if there's an exception during database operations
                Toast.makeText(this@AddToDoActivity, e.localizedMessage, Toast.LENGTH_LONG).show()
            }

            // Navigate back to MainActivity after saving
            val intentToMain = Intent(this@AddToDoActivity, MainActivity::class.java)
            intentToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intentToMain)
        } else {
            // Show a message if the title or content is empty
            Toast.makeText(this@AddToDoActivity, "Please Enter Title or Content", Toast.LENGTH_LONG).show()
        }
    }

    // Delete the selected ToDos item from the database
    fun delete(view: View?) {
        val intent = intent
        val selectedRecyclerRowItem = intent.getIntExtra("id", 1)

        // Delete the ToDos from the database
        dataBase.execSQL("DELETE FROM todos WHERE id = $selectedRecyclerRowItem")

        // Navigate back to MainActivity after deletion
        val intentToMain = Intent(this@AddToDoActivity, MainActivity::class.java)
        intentToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intentToMain)
    }
}
