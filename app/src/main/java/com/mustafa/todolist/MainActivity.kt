package com.mustafa.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.mustafa.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var toDoList : ArrayList<ToDo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Set up the toolbar
        var toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize the list of ToDos
        toDoList = ArrayList<ToDo>()
        getSqlData()

        // Set up the RecyclerView and its adapter
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        val toDoAdaptor = ToDoAdaptor(toDoList)
        binding.mainRecyclerView.adapter = toDoAdaptor
    }

    // Retrieve ToDos data from the SQLite database
    private fun getSqlData() {
        val getData = openOrCreateDatabase("ToDos", MODE_PRIVATE, null)
        val cursor = getData.rawQuery("SELECT * FROM todos", null)
        val toDoTitleIx = cursor.getColumnIndex("title")
        val toDoIdIx = cursor.getColumnIndex("id")

        while (cursor.moveToNext()) {
            val title = cursor.getString(toDoTitleIx)
            val id = cursor.getInt(toDoIdIx)
            val toDos = ToDo(title, id)
            toDoList.add(toDos)
        }
        cursor.close()
    }

    // Create options menu in the toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    // Handle options menu item selection
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.Add -> {
                // Start AddToDoActivity to add a new ToDos item
                addToDo()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Start AddToDoActivity to add a new ToDos item
    private fun addToDo() {
        val intentToAddToDo: Intent = Intent(this@MainActivity, AddToDoActivity::class.java)
        intentToAddToDo.putExtra("info", "new")
        startActivity(intentToAddToDo)
    }
}
