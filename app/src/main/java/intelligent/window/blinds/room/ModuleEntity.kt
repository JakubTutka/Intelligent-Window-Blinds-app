package intelligent.window.blinds.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "modules")
data class ModuleEntity(
    @PrimaryKey val id: Short,
    val ipAddress: String,
    val phr: Byte,
    var ser: Byte
)

