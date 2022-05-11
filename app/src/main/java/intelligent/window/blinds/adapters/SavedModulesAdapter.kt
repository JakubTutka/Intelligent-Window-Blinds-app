package intelligent.window.blinds.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import intelligent.window.blinds.EditModuleActivity
import intelligent.window.blinds.R
import intelligent.window.blinds.room.Module
import intelligent.window.blinds.room.ModuleEntity
import intelligent.window.blinds.room.ModuleViewModel
import intelligent.window.blinds.utils.convertModuleToEntity
import java.io.Serializable

class SavedModulesAdapter(private var mModuleViewModel: ModuleViewModel) : RecyclerView.Adapter<SavedModulesAdapter.SavedViewHolder>() {

    private var modules: List<Module> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_module_saved, parent, false)
        return SavedViewHolder(view).linkAdapter(this).linkViewModel(mModuleViewModel).linkContext(parent.context)
    }

    override fun onBindViewHolder(holder: SavedModulesAdapter.SavedViewHolder, position: Int) {
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

    class SavedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val moduleName: TextView = view.findViewById(R.id.item_saved_module_name)
        val moduleIP: TextView = view.findViewById(R.id.item_saved_module_ip)
        val moduleID: TextView = view.findViewById(R.id.item_saved_module_id)
        private val removeModelButton: Button = view.findViewById(R.id.item_remove_module_button)
        private val editModuleButton: Button = view.findViewById(R.id.item_edit_module_button)

        lateinit var module: Module
        private lateinit var adapter: SavedModulesAdapter
        private lateinit var mModuleViewModule: ModuleViewModel
        private lateinit var context: Context

        init {
            removeModelButton.setOnClickListener{
                Log.d("SavedModulesAdapter", "onClick on remove button on item: ${module.toString()}")
                removeModuleFromDatabase(convertModuleToEntity(module))
                adapter.notifyItemRemoved(adapterPosition)
            }

            editModuleButton.setOnClickListener {
                Log.d("SavedModulesAdapter", "onClick on edit button on item: ${module.toString()}")
                openEditModuleActivity(module)
            }
        }

        fun linkViewModel(viewModel: ModuleViewModel) : SavedViewHolder {
            this.mModuleViewModule = viewModel
            return this
        }

        fun linkAdapter(adapter: SavedModulesAdapter) : SavedViewHolder {
            this.adapter = adapter
            return this
        }

        fun linkContext(context: Context) : SavedViewHolder {
            this.context = context
            return this
        }

        fun removeModuleFromDatabase(module: ModuleEntity){
            mModuleViewModule.deleteModule(module)
        }

        private fun openEditModuleActivity(module: Module) {
            // TODO("Edit module, i.e. change local name, ID or delete from saved")
            val intent = Intent(context, EditModuleActivity::class.java).also{
                it.putExtra("EXTRA_MODULE", module as Serializable)
            }
            context.startActivity(intent)
        }

    }
}