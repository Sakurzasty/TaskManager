package pl.edu.pja.taskmanager.model

data class Task(
    val id: Long,
    val name: String,
    val priority: String,
    val progression: Int,
    val deadline: String,
    val time: Int,
    val status: String
)