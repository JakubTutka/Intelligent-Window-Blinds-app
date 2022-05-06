package intelligent.window.blinds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import intelligent.window.blinds.room.Module

class ScannedModulesAdapter(private val modules: List<Module>) : RecyclerView.Adapter<ScannedModulesAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScannedModulesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_module, parent, false)

        return ScannedModulesAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScannedModulesAdapter.ViewHolder, position: Int) {
        val id = modules[position].id
        val ip = modules[position].ipAddress.toString().drop(1)
        val idHex = "0x" + Integer.toHexString(id.toInt())
        val moduleName = ip + ":" + idHex
        holder.moduleName.text = moduleName
        holder.moduleIP.text = ip
        holder.moduleID.text = idHex
    }

    override fun getItemCount(): Int {
        return modules.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val moduleName: TextView = view.findViewById(R.id.item_module_name)
        val moduleIP: TextView = view.findViewById(R.id.item_module_ip)
        val moduleID: TextView = view.findViewById(R.id.item_module_id)
    }
}