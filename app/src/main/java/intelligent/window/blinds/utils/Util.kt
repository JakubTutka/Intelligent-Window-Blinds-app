package intelligent.window.blinds.utils

import intelligent.window.blinds.room.Module
import intelligent.window.blinds.room.ModuleEntity
import java.net.InetAddress

fun convertModuleToEntity(module: Module): ModuleEntity {
    return ModuleEntity(module.id, module.ipAddress.toString().drop(1), module.phr, module.ser)
}

fun convertListEntityToModule(entityModules: List<ModuleEntity>): List<Module> {
    val modules: MutableList<Module> = mutableListOf()
    for (entity in entityModules) {
        val ip: String = entity.ipAddress
        modules.add(Module(entity.id, InetAddress.getByName(ip), entity.phr, entity.ser))
    }

    return modules
}