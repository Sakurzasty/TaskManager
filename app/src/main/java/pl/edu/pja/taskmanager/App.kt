package pl.edu.pja.taskmanager

import android.app.Application
import pl.edu.pja.taskmanager.database.AppDatabase

class App : Application() {
    val database by lazy {AppDatabase.open(this)}

    override fun onCreate() {
        super.onCreate()
        database
    }
}