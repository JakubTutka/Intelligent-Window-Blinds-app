package intelligent.window.blinds.room

import androidx.lifecycle.LiveData

class ModuleRepository(private val moduleDao: ModuleDao) {

    val readAllData: LiveData<List<ModuleEntity>> = moduleDao.readAllModules()
    val readAllId: LiveData<List<Short>> = moduleDao.readAllId()

    suspend fun addModule(module: ModuleEntity){
        moduleDao.addModule(module)
    }

    suspend fun deleteModule(module: ModuleEntity){
        moduleDao.deleteModule(module)
    }

}