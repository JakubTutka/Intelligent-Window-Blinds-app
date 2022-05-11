package intelligent.window.blinds.room

import java.io.Serializable
import java.net.InetAddress

data class Module(
    var id: Short,
    var ipAddress: InetAddress,
    var phr: Byte,
    var ser: Byte,
    var isAdaptive: Boolean = false) : Serializable {
    override fun toString(): String {
        return "Module IP: %s, ID: 0x%X, PHR: 0x%02X, SER: 0x%02X".format(this.ipAddress.toString().drop(1), this.id, this.phr, this.ser)
    }
}