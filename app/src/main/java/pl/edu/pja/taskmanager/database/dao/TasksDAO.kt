package pl.edu.pja.taskmanager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pl.edu.pja.taskmanager.model.dto.TaskDTO

@Dao
interface TasksDAO {

    @Insert()
    fun add(taskDTO: TaskDTO)

    @Query("UPDATE taskdto SET status = 'completed' where id = :taskId")
    fun update(taskId: Long)

    @Query("SELECT * from taskdto")
    fun getAll(): List<TaskDTO>

    @Query("SELECT * from taskdto where deadline >= :startDate and deadline <= :endDate and status = 'nowe'")
    fun getAllFromRange(startDate: String, endDate:String): List<TaskDTO>
}