package intelligent.window.blinds.utils

import java.net.InetAddress

data class Module(
    var ipAddress: InetAddress,
    var id: Short,
    var phr: Byte,
    var ser: Byte) {
    override fun toString(): String {
        return "Module IP: %s, ID: 0x%X, PHR: 0x%02X, SER: 0x%02X".format(this.ipAddress.toString(), this.id, this.phr, this.ser)
    }
}