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

fun convertEntityToModule(entity: ModuleEntity): Module {
    val ip: String = entity.ipAddress
    return Module(entity.id, InetAddress.getByName(ip), entity.phr, entity.ser, entity.isAdaptive)
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

fun Boolean.toInt() = if (this) 1 else 0

fun convertLevelToPercentage(level: Int): Int {
    return ((level.toDouble()/255)*100).toInt()
}

fun convertPercentageToLevel(percent: Int): Int {
    return ((percent.toDouble()/100)*255).toInt()
}

fun mapTempToSer(temp: Double): Int {
    if (temp > 30){
        return 0
    } else if (temp < 30 && temp >= 20) {
        return 3
    } else if (temp < 20 && temp >= 10) {
        return 5
    } else if (temp < 10) {
        return 10
    }
    return 0
}

fun mapCloudToSer(cloud: Int): Int {
    return when {
        cloud > 75 -> {
            0
        }
        cloud in 50..75 -> {
            5
        }
        cloud in 1..49 -> {
            10
        }
        else -> 0
    }
}

fun calculateSer(icon: String, temp: Double, cloud: Int): Int {
    if (iconToSerMap[icon] == null) {
        val iconSer: Int = 50
        return (iconSer + mapTempToSer(temp) + mapCloudToSer(cloud))
    } else {
        val iconSer: Int = iconToSerMap[icon]!!
        return (iconSer + mapTempToSer(temp) + mapCloudToSer(cloud))
    }

}