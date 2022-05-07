package intelligent.window.blinds.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ModuleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addModule(moduleEntity: ModuleEntity)

    @Query("SELECT * FROM modules ORDER BY id ASC")
    fun readAllModules(): LiveData<List<ModuleEntity>>

    @Delete
    suspend fun deleteModule(moduleEntity: ModuleEntity)
}