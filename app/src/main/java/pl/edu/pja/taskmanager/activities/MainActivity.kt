package pl.edu.pja.taskmanager.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pja.taskmanager.App
import pl.edu.pja.taskmanager.R
import pl.edu.pja.taskmanager.adapter.TaskAdapter
import pl.edu.pja.taskmanager.databinding.ActivityMainBinding
import pl.edu.pja.taskmanager.model.dto.TaskDTO
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoField
import java.util.*

private const val REQUEST_TASK = 1

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    private val database by lazy {(application as App).database}
    private val taskAdapter by lazy { TaskAdapter(database) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupTaskList()
        setupAddButton()
        setupWeek()
        setupCurrentCountTask()
        setupPreparedData()
    }

    private fun setupTaskList(){
        binding.recycleList.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
            taskAdapter.setOnItemClickListener(object: TaskAdapter.onClickListener{
                override fun onItemClick(position: Int) {
                    val task = taskAdapter.getTask(position)
                    val intent = Intent(context, TaskSummaryActivity::class.java)
                    intent.putExtra("id", task.id)
                    intent.putExtra("name", task.name)
                    intent.putExtra("priority", task.priority)
                    intent.putExtra("progression", task.progression)
                    intent.putExtra("deadline", task.deadline)
                    intent.putExtra("time", task.time.toString())
                    intent.putExtra("status", task.status)
                    startActivityForResult(
                        intent, REQUEST_TASK
                    )
                }

                override fun onItemLongClick(position: Int) {
                    val positiveButtonClick = {
                            dialog: DialogInterface, which: Int -> removeTask(position)
                    }
                    val negativeButtonClick = {
                            dialog: DialogInterface, which: Int -> dialog.dismiss()
                    }

                    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                    builder.setTitle("Zakończ zadanie")
                    builder.setMessage("Czy zakończyć zadanie?")
                    builder.setPositiveButton("TAK", DialogInterface.OnClickListener(positiveButtonClick))
                    builder.setNegativeButton("NIE", DialogInterface.OnClickListener(negativeButtonClick))
                    builder.show()
                }
            })

            val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(getDrawable(R.drawable.divider)!!)
            addItemDecoration(itemDecoration)
        }
        taskAdapter.reload()
    }

    private fun removeTask(position: Int){
        taskAdapter.deactivateTask(position)
        finish()
        overridePendingTransition(0,0)
        startActivity(intent)
        overridePendingTransition(0,0)
    }

    private fun setupAddButton(){
        binding.addButton.setOnClickListener{
            startActivityForResult(
                Intent(this, TaskNewFormActivity::class.java), REQUEST_TASK
            )
        }
    }

    private fun setupWeek(){
        binding.week.text = getActualWeek().toString()
    }

    private fun setupCurrentCountTask(){
        binding.countTask.text = getCurrentCountTask().toString()
    }

    private fun setupPreparedData(){
        if (getCurrentCountTask() == 0) {
            val task = TaskDTO(
                0,
                "INIT TASK 01",
                "wysoki",
                50,
                LocalDate.now().toString(),
                20,
                "nowe"
            )
            database.records.add(task)
            finish()
            overridePendingTransition(0,0)
            startActivity(intent)
            overridePendingTransition(0,0)
        }
    }

    fun getCurrentCountTask(): Int {
        val actualDayOfWeek = LocalDate.now().toString()
        val lastOfWeek = LocalDateTime.now().with(ChronoField.DAY_OF_WEEK , 7).toLocalDate().toString()
        return database.records.getAllFromRange(actualDayOfWeek, lastOfWeek).size
    }

    private fun getActualWeek() : Int{
        return Calendar.getInstance(TimeZone.getTimeZone("UTC")).get(Calendar.WEEK_OF_YEAR)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        taskAdapter.reload()
        setupCurrentCountTask()
        super.onActivityResult(requestCode, resultCode, data)
    }


}