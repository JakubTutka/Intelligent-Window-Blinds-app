package intelligent.window.blinds.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ModuleEntity::class], version = 2, exportSchema = false)
abstract class ModuleDatabase : RoomDatabase() {

    abstract fun moduleDao(): ModuleDao

    companion object{

        val migration_1_2: Migration = object: Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE modules ADD COLUMN isAdaptive INTEGER NOT NULL DEFAULT (0)")
            }
        }

        @Volatile
        private var INSTANCE: ModuleDatabase? = null

        fun getDatabase(context: Context): ModuleDatabase{
            val tempInstance = INSTANCE
            if(tempInstance!= null) {
                return tempInstance

            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ModuleDatabase::class.java,
                    "module_database"
                ).addMigrations(migration_1_2).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}