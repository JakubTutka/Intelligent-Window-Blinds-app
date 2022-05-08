package intelligent.window.blinds

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout

class EditModuleActivity : AppCompatActivity() {

    private val TAG = "EditModuleActivity"
    private lateinit var rootLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_module)

        rootLayout = findViewById(R.id.root_edit_layout)

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "ON RESUME")
    }
}