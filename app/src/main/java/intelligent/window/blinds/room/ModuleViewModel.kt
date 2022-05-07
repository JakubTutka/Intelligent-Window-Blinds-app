package intelligent.window.blinds.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ModuleViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<ModuleEntity>>
    private val repository: ModuleRepository

    init {
        val moduleDao = ModuleDatabase.getDatabase(application).moduleDao()
        repository = ModuleRepository(moduleDao)
        readAllData = repository.readAllData
    }

    fun addModule(module: ModuleEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addModule(module)
        }
    }
}