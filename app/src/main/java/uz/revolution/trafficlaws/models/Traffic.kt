package uz.revolution.trafficlaws.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "traffic")
class Traffic : Serializable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int? = null

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "info")
    var info: String? = null

    @ColumnInfo(name = "imagePath")
    var imagePath: String? = null

    @ColumnInfo(name = "category")
    var category: Int? = null

    @ColumnInfo(name = "liked")
    var liked: Boolean? = null

    @Ignore
    constructor(name: String?, info: String?, imagePath: String?, category: Int?, liked: Boolean?) {
        this.name = name
        this.info = info
        this.imagePath = imagePath
        this.category = category
        this.liked = liked
    }

    constructor()

}