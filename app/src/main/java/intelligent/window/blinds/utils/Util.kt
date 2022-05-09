package intelligent.window.blinds.utils

import intelligent.window.blinds.room.Module
import intelligent.window.blinds.room.ModuleEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.InetAddress

fun convertModuleToEntity(module: Module): ModuleEntity {
    return ModuleEntity(module.id, module.ipAddress.toString().drop(1), module.phr, module.ser, module.isAdaptive)
}

fun convertListEntityToModule(entityModules: List<ModuleEntity>): List<Module> {
    val modules: MutableList<Module> = mutableListOf()
    for (entity in entityModules) {
        val ip: String = entity.ipAddress
        modules.add(Module(entity.id, InetAddress.getByName(ip), entity.phr, entity.ser, entity.isAdaptive))
    }

    return modules
}

fun Byte.toPositiveInt() = toInt() and 0xFF

fun convertLevelToPercentage(level: Int): Int {
    return ((level.toDouble()/255)*100).toInt()
}

fun convertPercentageToLevel(percent: Int): Int {
    return ((percent.toDouble()/100)*255).toInt()
}