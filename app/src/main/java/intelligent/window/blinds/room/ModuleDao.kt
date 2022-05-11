package intelligent.window.blinds.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ModuleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addModule(moduleEntity: ModuleEntity)

    @Query("SELECT * FROM modules ORDER BY id ASC")
    fun readAllModules(): LiveData<List<ModuleEntity>>

    @Update
    suspend fun updateModule(moduleEntity: ModuleEntity)

    @Delete
    suspend fun deleteModule(moduleEntity: ModuleEntity)

    @Query("SELECT id FROM modules")
    fun readAllId(): LiveData<List<Short>>

    @Query("UPDATE modules SET isAdaptive = :isAdaptive WHERE id = :id")
    fun updateAdaptiveColumn(isAdaptive: Int, id: Short)
}