package com.example.beod

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.plusAssign
import com.example.beod.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val tasksList = mutableListOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        with(binding) {
            setContentView(root)
            fab.setOnClickListener { _ ->
                val taskItem = layoutInflater.inflate(R.layout.item_task, null)
                tasks += taskItem
                tasksList += taskItem
            }
            start.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    val checkType = if (eod.isChecked) "EOD" else "BOD"
                    val resTasks = tasksList.map {
                        val taskName = it.findViewById<EditText>(R.id.task_name).text.toString()
                        val taskCount = it.findViewById<EditText>(R.id.task_count).text.toString()
                        val taskTime = it.findViewById<EditText>(R.id.task_time).text.toString()
                        Task(taskName, taskCount, taskTime)
                    }
                    val eodBod = getBODEOD(checkType, resTasks)
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, eodBod)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }

    fun getBODEOD(
        type: String,
        tasks: List<Task>,
        date: String = SimpleDateFormat("dd/M/yyyy", Locale.ENGLISH).format(Date())
    ): String = "$type $date\n" +
            "${tasks.fold("") { acc, task -> "$acc -${task.name}\nCount: ${task.count}\nTime: ${task.time} \n\n" }}\n"


}

data class Task(val name: String, val count: String, val time: String)