package uz.revolution.trafficlaws.app

import android.app.Application
import uz.revolution.trafficlaws.database.AppDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.init(this)
    }
}