package uz.revolution.trafficlaws.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.revolution.trafficlaws.daos.TrafficDao
import uz.revolution.trafficlaws.models.Traffic

@Database(entities = [Traffic::class],version = 1,exportSchema = false)
abstract class AppDatabase:RoomDatabase() {
    abstract fun trafficDao():TrafficDao

    companion object{
        @Volatile
        private var database:AppDatabase?=null

        fun init(context: Context) {
            synchronized(this) {
                if (database == null) {
                    database = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "traffic.db"
                    )
                        .allowMainThreadQueries()
                        .build()
                }
            }
        }
    }

    object get{
        fun getDatabse():AppDatabase{
            return database!!
        }
    }
}