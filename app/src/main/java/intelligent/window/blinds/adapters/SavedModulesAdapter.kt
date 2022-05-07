package intelligent.window.blinds.adapters

import android.annotation.SuppressLint
import android.content.Context
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

class SavedModulesAdapter(private var mModuleViewModel: ModuleViewModel) : RecyclerView.Adapter<SavedModulesAdapter.ViewHolder>() {

    private var modules = emptyList<Module>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_module, parent, false)
        return ViewHolder(view).linkViewModel(mModuleViewModel).linkContext(parent.context)
    }

    override fun onBindViewHolder(holder: SavedModulesAdapter.ViewHolder, position: Int) {
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

    @SuppressLint("NotifyDataSetChanged")
    fun setData(module: List<Module>){
        this.modules = module
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val moduleName: TextView = view.findViewById(R.id.item_saved_module_name)
        val moduleIP: TextView = view.findViewById(R.id.item_saved_module_ip)
        val moduleID: TextView = view.findViewById(R.id.item_saved_module_id)
        private val removeModelButton: Button = view.findViewById(R.id.item_remove_module_button)

        lateinit var module: Module
        private lateinit var mModuleViewModule: ModuleViewModel
        private lateinit var context: Context

        init {
            removeModelButton.setOnClickListener{
                Log.d("demo", "onClick on remove button on item: ${module.toString()}")
            }
        }

        fun linkViewModel(viewModel: ModuleViewModel) : ViewHolder {
            this.mModuleViewModule = viewModel
            return this
        }

        fun linkContext(context: Context) : SavedModulesAdapter.ViewHolder {
            this.context = context
            return this
        }

    }
}