package intelligent.window.blinds.utils

import intelligent.window.blinds.room.Module
import intelligent.window.blinds.room.ModuleEntity

fun convertModuleToEntity(module: Module): ModuleEntity {
    return ModuleEntity(module.id, module.ipAddress.toString().drop(1), module.phr, module.ser)
}