package pl.edu.pja.taskmanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.edu.pja.taskmanager.database.dao.TasksDAO
import pl.edu.pja.taskmanager.model.dto.TaskDTO

private const val DATABASE_FILENAME = "records"

@Database(entities = [TaskDTO::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val records: TasksDAO

    companion object {
        fun open(context: Context): AppDatabase =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_FILENAME
            ).build()
    }
}