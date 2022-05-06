package intelligent.window.blinds.utils

import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import intelligent.window.blinds.R

object ModuleJson {

    var gson = Gson()

    fun serializeModule (module: Module) {

    }

    fun getListOfSavedModules(resources: Resources): Unit {
        val jsonString = resources.openRawResource(R.raw.devices)
            .bufferedReader().use { it.readText() }
        val modules = gson.fromJson(jsonString, Array<Module>::class.java).toList()
        for (module in modules) {
            println(module.toString())
        }
    }

}
