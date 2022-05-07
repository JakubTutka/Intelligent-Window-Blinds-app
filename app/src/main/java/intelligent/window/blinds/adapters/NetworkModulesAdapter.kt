package intelligent.window.blinds.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import intelligent.window.blinds.R
import intelligent.window.blinds.room.Module
import intelligent.window.blinds.room.ModuleEntity
import intelligent.window.blinds.room.ModuleViewModel
import intelligent.window.blinds.utils.convertModuleToEntity

class NetworkModulesAdapter(private var modules: MutableList<Module>, private var mModuleViewModel: ModuleViewModel) : RecyclerView.Adapter<NetworkModulesAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_module, parent, false)
        return ViewHolder(view).linkAdapter(this).linkViewModel(mModuleViewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val module = modules[position]
        val id = modules[position].id
        val ip = modules[position].ipAddress.toString().drop(1)
        val idHex = "0x" + Integer.toHexString(id.toInt())
        val moduleName = "$ip:$idHex"

        holder.moduleName.text = moduleName
        holder.moduleIP.text = ip
        holder.moduleID.text = idHex
        holder.module = module
    }

    override fun getItemCount(): Int {
        return modules.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val moduleName: TextView = view.findViewById(R.id.item_module_name)
        val moduleIP: TextView = view.findViewById(R.id.item_module_ip)
        val moduleID: TextView = view.findViewById(R.id.item_module_id)
        private val addModuleButton: Button = view.findViewById(R.id.item_module_button)

        lateinit var module: Module
        private lateinit var adapter: NetworkModulesAdapter
        private lateinit var mModuleViewModule: ModuleViewModel

        init {
            addModuleButton.setOnClickListener{
                Log.d("demo", "onClick: Add for module ${module.toString()}")
                adapter.modules.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
                addModuleToDatabase(convertModuleToEntity(module))
            }
        }

        fun linkAdapter(adapter: NetworkModulesAdapter) : ViewHolder {
            this.adapter = adapter
            return this
        }

        fun linkViewModel(viewModel: ModuleViewModel) : ViewHolder {
            this.mModuleViewModule = viewModel
            return this
        }

        private fun addModuleToDatabase(moduleEntity: ModuleEntity) {
            mModuleViewModule.addModule(moduleEntity)
            Log.d("demo", "added to database: ${module.toString()}")
        }

    }
}