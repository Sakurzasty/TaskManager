package pl.edu.pja.taskmanager.model.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.edu.pja.taskmanager.model.Task

@Entity
data class TaskDTO(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val priority: String,
    val progression: Int,
    val deadline: String,
    val status: String
) {
    fun toTaskModel(): Task {
        return Task(id, name, priority, progression, deadline, status)
    }
}