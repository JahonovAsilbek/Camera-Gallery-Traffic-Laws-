package uz.revolution.trafficlaws.daos

import androidx.room.*
import uz.revolution.trafficlaws.models.Traffic

@Dao
interface TrafficDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTraffic(traffic: Traffic)

    @Query("SELECT * FROM traffic")
    fun getAllTraffic(): List<Traffic>

    @Query("SELECT * FROM traffic WHERE category=:category")
    fun getTrafficByCategory(category: Int): List<Traffic>

    @Query("UPDATE traffic SET name=:name, info=:info, imagePath=:imagePath, category=:category,liked=:liked WHERE id=:id")
    fun updateTraffic(
        name: String,
        info: String,
        imagePath: String,
        category: Int,
        liked: Boolean,
        id: Int
    )

    @Delete
    fun deleteTraffic(traffic: Traffic)

}